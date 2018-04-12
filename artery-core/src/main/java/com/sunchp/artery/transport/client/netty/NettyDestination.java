package com.sunchp.artery.transport.client.netty;

import com.sunchp.artery.rpc.Response;
import com.sunchp.artery.rpc.ResponsePromise;
import com.sunchp.artery.serialize.ProtobufSerializeUtils;
import com.sunchp.artery.transport.client.Connection;
import com.sunchp.artery.transport.client.Destination;
import com.sunchp.artery.transport.codec.NettyDecoder;
import com.sunchp.artery.transport.codec.NettyEncoder;
import com.sunchp.artery.transport.codec.NettyMessage;
import com.sunchp.artery.utils.component.AbstractLifeCycle;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class NettyDestination extends AbstractLifeCycle implements Destination {
    private static final Logger LOGGER = LoggerFactory.getLogger(NettyDestination.class);
    private static final NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();

    private final InetSocketAddress address;
    private final ConcurrentMap<String, ResponsePromise> callbackMap = new ConcurrentHashMap<String, ResponsePromise>();
    private Bootstrap bootstrap;

    public NettyDestination(InetSocketAddress address) {
        this.address = address;
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
                        pipeline.addLast("handler", new NettyDestinationChannelAdapter(NettyDestination.this));
                    }
                });
    }

    protected void doStop() throws Exception {
        callbackMap.clear();
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return this.address;
    }

    @Override
    public Connection newConnection() {
        try {
            ChannelFuture future = bootstrap.connect(this.address).sync();
            return new NettyConnection(future.channel(), this);
        } catch (Exception e) {
            LOGGER.error("", e);
        }

        return null;
    }

    public void registerCallback(String requestId, ResponsePromise responsePromise) {
        this.callbackMap.put(requestId, responsePromise);
    }

    public ResponsePromise removeCallback(String requestId) {
        return callbackMap.remove(requestId);
    }

    public void processMessage(NettyMessage msg) {
        try {
            Response response = ProtobufSerializeUtils.deserialize(msg.getData(), Response.class);
            ResponsePromise responsePromise = callbackMap.remove(response.getRequestId());

            if (responsePromise == null) {
                LOGGER.warn("NettyClient has response from server, but responseFuture not exist, requestId={}", response.getRequestId());
                return;
            }
            if (response.getException() != null) {
                responsePromise.failed(response.getException());
            } else {
                responsePromise.succeeded(response.getValue());
            }
        } catch (Exception e) {
            LOGGER.error("", e);
        }
    }
}
