package com.sunchp.artery.transport.server.handler;

import com.sunchp.artery.transport.codec.NettyMessage;
import io.netty.channel.ChannelHandlerContext;

public interface Handler {
    public void handle(ChannelHandlerContext ctx, NettyMessage msg);
}
