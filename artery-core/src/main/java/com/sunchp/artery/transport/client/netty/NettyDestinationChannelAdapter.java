package com.sunchp.artery.transport.client.netty;

import com.sunchp.artery.transport.client.Destination;
import com.sunchp.artery.transport.codec.NettyMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyDestinationChannelAdapter extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(NettyDestinationChannelAdapter.class);

    private final Destination destination;

    public NettyDestinationChannelAdapter(Destination destination) {
        this.destination = destination;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof NettyMessage) {
            destination.processMessage((NettyMessage) msg);
        } else {
            LOGGER.error("NettyDestinationChannelAdapter messageReceived type not support: class={}", msg.getClass());
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("NettyDestinationChannelAdapter channelActive: remote={} local={}", ctx.channel().remoteAddress(), ctx.channel().localAddress());
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("NettyDestinationChannelAdapter channelInactive: remote={} local={}", ctx.channel().remoteAddress(), ctx.channel().localAddress());
        ctx.fireChannelInactive();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("NettyDestinationChannelAdapter exceptionCaught: remote={} local={} event={}", ctx.channel().remoteAddress(), ctx.channel().localAddress(), cause.getMessage(), cause);
        ctx.channel().close();
    }
}
