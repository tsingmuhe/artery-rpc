package com.sunchp.artery.transport.netty;

import com.sunchp.artery.transport.EndpointState;
import com.sunchp.artery.rpc.Request;
import com.sunchp.artery.rpc.ResponsePromise;
import com.sunchp.artery.transport.AbstractClient;
import com.sunchp.artery.transport.TransportException;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class NettyClient extends AbstractClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(NettyClient.class);

    private static final NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();
    private Bootstrap bootstrap;
    private final ConcurrentMap<String, ResponsePromise> callbackMap = new ConcurrentHashMap<String, ResponsePromise>();

    private NettyClientConnection clientConnection;
    private int connections;

    public NettyClient(SocketAddress remoteAddress, int connections) {
        super(remoteAddress);
        this.connections = connections;
    }

    public Bootstrap getBootstrap() {
        return bootstrap;
    }

    @Override
    public ResponsePromise call(Request request) throws TransportException {
        return this.clientConnection.call(request);
    }

    @Override
    public synchronized void start() throws IOException {
        if (isAvailable()) {
            LOGGER.warn("the channel already open, remote: " + remoteAddress);
        }

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


        this.clientConnection = new NettyClientConnection(this);
        try {
            this.clientConnection.start();
        } catch (Exception e) {
            LOGGER.error("NettyClient init pool create connect Error: remoteAddress=" + remoteAddress, e);
        }
    }

    @Override
    public synchronized void shutdown() {
        if (state.isCloseState()) {
            LOGGER.info("NettyClient close fail: already close, remoteAddress={}", remoteAddress);
            return;
        }

        if (state.isUnInitState()) {
            LOGGER.info("NettyClient close Fail: don't need to close because node is unInit state: remoteAddress={}", remoteAddress);
            return;
        }

        this.clientConnection.shutdown();

        // 清空callback
        callbackMap.clear();

        // 设置close状态
        state = EndpointState.CLOSE;
    }


    public void registerCallback(String requestId, ResponsePromise responsePromise) {
        this.callbackMap.put(requestId, responsePromise);
    }

    public ResponsePromise removeCallback(String requestId) {
        return callbackMap.remove(requestId);
    }
}
