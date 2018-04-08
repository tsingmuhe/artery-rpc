package com.sunchp.artery.transport.netty;

import com.sunchp.artery.rpc.Request;
import com.sunchp.artery.rpc.ResponsePromise;
import com.sunchp.artery.serialize.ProtobufSerializeUtils;
import com.sunchp.artery.transport.TransportException;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class NettyClientConnection {
    private static final Logger LOGGER = LoggerFactory.getLogger(NettyClientConnection.class);

    private final NettyClient client;
    private Channel channel = null;

    public NettyClientConnection(NettyClient client) {
        this.client = client;
    }

    public ResponsePromise call(Request request) throws TransportException {
        ResponsePromise responsePromise = new ResponsePromise(request);
        this.client.registerCallback(request.getRequestId(), responsePromise);

        ChannelFuture writeFuture = this.channel.writeAndFlush(ProtobufSerializeUtils.serialize(request, Request.class));
        boolean result = writeFuture.awaitUninterruptibly(200, TimeUnit.MILLISECONDS);

        if (result && writeFuture.isSuccess()) {
            return responsePromise;
        }

        writeFuture.cancel(true);
        this.client.removeCallback(request.getRequestId());
        responsePromise.cancel(true);

        if (writeFuture.cause() != null) {
            throw new TransportException("NettyChannel send request to server Error.", writeFuture.cause());
        }

        throw new TransportException("NettyChannel send request to server Timeout.");
    }

    public void start() throws IOException, InterruptedException {
        ChannelFuture future = client.getBootstrap().connect(this.client.getRemoteAddress()).sync();
        channel = future.channel();
    }

    public void shutdown() {
        try {
            if (channel != null) {
                channel.close();
                channel = null;
            }
        } catch (Exception e) {
            LOGGER.error("NettyChannel close Error. ", e);
        }
    }
}
