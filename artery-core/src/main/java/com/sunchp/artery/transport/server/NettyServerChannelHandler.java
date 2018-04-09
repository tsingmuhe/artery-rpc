package com.sunchp.artery.transport.server;

import com.sunchp.artery.rpc.Request;
import com.sunchp.artery.rpc.Response;
import com.sunchp.artery.serialize.ProtobufSerializeUtils;
import com.sunchp.artery.transport.codec.NettyMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyServerChannelHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(NettyServerChannelHandler.class);

    private final Server server;

    public NettyServerChannelHandler(Server server) {
        this.server = server;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof NettyMessage) {
            processMessage(ctx, (NettyMessage) msg);
        } else {
            LOGGER.error("NettyServerChannelHandler messageReceived type not support: class={}", msg.getClass());
        }
    }

    private void processMessage(ChannelHandlerContext ctx, NettyMessage msg) {
        Request request = ProtobufSerializeUtils.deserialize(msg.getData(), Request.class);
        Response response = new Response();
        response.setRequestId(request.getRequestId());
        try {
            server.handle(request, response);
        } catch (Throwable t) {
            response.setException(t);
            LOGGER.error("RPC Server handle request error", t);
        }
        ctx.writeAndFlush(ProtobufSerializeUtils.serialize(response, Response.class));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("NettyServerChannelHandler channelActive: remote={} local={}", ctx.channel().remoteAddress(), ctx.channel().localAddress());
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("NettyServerChannelHandler channelInactive: remote={} local={}", ctx.channel().remoteAddress(), ctx.channel().localAddress());
        ctx.fireChannelInactive();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("NettyServerChannelHandler exceptionCaught: remote={} local={} event={}", ctx.channel().remoteAddress(), ctx.channel().localAddress(), cause.getMessage(), cause);
        ctx.channel().close();
    }
}