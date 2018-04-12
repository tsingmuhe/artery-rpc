package com.sunchp.artery.transport.server.handler;

import com.sunchp.artery.rpc.Request;
import com.sunchp.artery.rpc.Response;
import com.sunchp.artery.serialize.ProtobufSerializeUtils;
import com.sunchp.artery.transport.codec.NettyMessage;
import io.netty.channel.ChannelHandlerContext;

public abstract class AbstractHandler implements Handler {
    public void handle(ChannelHandlerContext ctx, NettyMessage msg) {
        handleMessage(ctx, msg);
    }

    protected void handleMessage(ChannelHandlerContext ctx, NettyMessage msg) {
        Request request = ProtobufSerializeUtils.deserialize(msg.getData(), Request.class);
        Response response = new Response();
        response.setRequestId(request.getRequestId());
        processMessage(request, response);
        ctx.writeAndFlush(ProtobufSerializeUtils.serialize(response, Response.class));
    }

    protected abstract void processMessage(Request request, Response response);
}
