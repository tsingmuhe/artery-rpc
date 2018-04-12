package com.sunchp.artery.transport.server;

import com.sunchp.artery.transport.codec.NettyMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyServerChannelAdapter extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(NettyServerChannelAdapter.class);

    private final NettyServer server;

    public NettyServerChannelAdapter(NettyServer server) {
        this.server = server;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof NettyMessage) {
            server.handle(ctx, (NettyMessage) msg);
        } else {
            LOGGER.error("NettyServerChannelAdapter messageReceived type not support: class={}", msg.getClass());
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("NettyServerChannelAdapter channelActive: remote={} local={}", ctx.channel().remoteAddress(), ctx.channel().localAddress());
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("NettyServerChannelAdapter channelInactive: remote={} local={}", ctx.channel().remoteAddress(), ctx.channel().localAddress());
        ctx.fireChannelInactive();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("NettyServerChannelAdapter exceptionCaught: remote={} local={} event={}", ctx.channel().remoteAddress(), ctx.channel().localAddress(), cause.getMessage(), cause);
        ctx.channel().close();
    }
}