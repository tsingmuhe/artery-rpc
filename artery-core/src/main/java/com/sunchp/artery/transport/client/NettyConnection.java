package com.sunchp.artery.transport.client;

import com.sunchp.artery.rpc.Request;
import com.sunchp.artery.rpc.ResponsePromise;
import com.sunchp.artery.serialize.ProtobufSerializeUtils;
import com.sunchp.artery.transport.TransportException;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

import java.util.concurrent.TimeUnit;

public class NettyConnection implements Connection {
    private final Channel channel;
    private final NettyDestination nettyDestination;

    public NettyConnection(Channel channel, NettyDestination nettyDestination) {
        this.channel = channel;
        this.nettyDestination = nettyDestination;
    }

    @Override
    public ResponsePromise send(Request request) {
        ResponsePromise responsePromise = new ResponsePromise(request);
        this.nettyDestination.registerCallback(request.getRequestId(), responsePromise);

        ChannelFuture writeFuture = this.channel.writeAndFlush(ProtobufSerializeUtils.serialize(request, Request.class));
        boolean result = writeFuture.awaitUninterruptibly(200, TimeUnit.MILLISECONDS);

        if (result && writeFuture.isSuccess()) {
            return responsePromise;
        }

        writeFuture.cancel(true);
        this.nettyDestination.removeCallback(request.getRequestId());
        responsePromise.cancel(true);

        if (writeFuture.cause() != null) {
            throw new TransportException("NettyChannel send request to server Error.", writeFuture.cause());
        }

        throw new TransportException("NettyChannel send request to server Timeout.");
    }

    @Override
    public void close() {
        this.channel.close();
    }

    @Override
    public boolean isClosed() {
        return false;
    }
}
