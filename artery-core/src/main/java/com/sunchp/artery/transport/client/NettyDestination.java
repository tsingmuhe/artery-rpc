package com.sunchp.artery.transport.client;

import com.sunchp.artery.rpc.ResponsePromise;
import com.sunchp.artery.transport.codec.NettyDecoder;
import com.sunchp.artery.transport.codec.NettyEncoder;
import com.sunchp.artery.utils.Promise;
import com.sunchp.artery.utils.component.AbstractLifeCycle;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class NettyDestination extends AbstractLifeCycle implements Destination {
    private final String host;
    private final int port;

    private static final NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();
    private final ConcurrentMap<String, ResponsePromise> callbackMap = new ConcurrentHashMap<String, ResponsePromise>();
    private Bootstrap bootstrap;

    public NettyDestination(String host, int port) {
        this.host = host;
        this.port = port;
    }

    protected void doStart() throws Exception {
        bootstrap = new Bootstrap();
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000);
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.group(nioEventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast("decoder", new NettyDecoder());
                        pipeline.addLast("encoder", new NettyEncoder());
                        pipeline.addLast("handler", new NettyClientChannelHandler(callbackMap));
                    }
                });
    }

    protected void doStop() throws Exception {
        callbackMap.clear();
    }

    @Override
    public String getHost() {
        return this.host;
    }

    @Override
    public int getPort() {
        return this.port;
    }

    @Override
    public void newConnection(Promise<Connection> promise) {
        try {
            ChannelFuture future = bootstrap.connect(host, port).sync();
            promise.succeeded(new NettyConnection(future.channel(), this));
        } catch (InterruptedException e) {
            promise.failed(e);
        }
    }

    public NettyConnection newConnection() {
        try {
            ChannelFuture future = bootstrap.connect(host, port).sync();
            return new NettyConnection(future.channel(), this);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void registerCallback(String requestId, ResponsePromise responsePromise) {
        this.callbackMap.put(requestId, responsePromise);
    }

    public ResponsePromise removeCallback(String requestId) {
        return callbackMap.remove(requestId);
    }
}
