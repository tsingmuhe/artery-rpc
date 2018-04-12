package com.sunchp.artery.transport.server.handler;

import com.sunchp.artery.rpc.ExporterContainer;
import com.sunchp.artery.transport.codec.NettyMessage;
import com.sunchp.artery.utils.QueuedThreadPool;
import io.netty.channel.ChannelHandlerContext;

public class ThreadPoolExporterContainerHandler extends ExporterContainerHandler {
    private final QueuedThreadPool threadPool;

    public ThreadPoolExporterContainerHandler(ExporterContainer exporterContainer, QueuedThreadPool threadPool) {
        super(exporterContainer);
        this.threadPool = threadPool;
    }

    @Override
    public void handle(ChannelHandlerContext ctx, NettyMessage msg) {
        this.threadPool.execute(new Runnable() {
            @Override
            public void run() {
                handleMessage(ctx, msg);
            }
        });
    }
}
