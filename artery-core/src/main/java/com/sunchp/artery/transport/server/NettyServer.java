package com.sunchp.artery.transport.server;

import com.sunchp.artery.exporter.DefaultExporterContainer;
import com.sunchp.artery.transport.codec.NettyDecoder;
import com.sunchp.artery.transport.codec.NettyEncoder;
import com.sunchp.artery.transport.codec.NettyMessage;
import com.sunchp.artery.transport.server.handler.Handler;
import com.sunchp.artery.transport.server.handler.ThreadPoolExporterContainerHandler;
import com.sunchp.artery.utils.QueuedThreadPool;
import com.sunchp.artery.utils.component.AbstractLifeCycle;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class NettyServer extends AbstractLifeCycle implements Handler {
    private static final Logger LOG = LoggerFactory.getLogger(NettyServer.class);

    private final InetSocketAddress address;
    private final QueuedThreadPool threadPool;
    private final Handler handler;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private Channel serverChannel;

    public NettyServer(InetSocketAddress address) {
        this(address, (QueuedThreadPool) null, (Handler) null);
    }

    public NettyServer(InetSocketAddress address, Handler handler) {
        this(address, (QueuedThreadPool) null, handler);
    }

    public NettyServer(InetSocketAddress address, QueuedThreadPool threadPool, Handler handler) {
        this.address = address;
        this.threadPool = threadPool != null ? threadPool : new QueuedThreadPool();
        this.handler = handler != null ? handler : new ThreadPoolExporterContainerHandler(new DefaultExporterContainer(), this.threadPool);
    }

    public QueuedThreadPool getThreadPool() {
        return this.threadPool;
    }

    @Override
    protected void doStart() throws Exception {
        if (bossGroup == null) {
            bossGroup = new NioEventLoopGroup(1);
            workerGroup = new NioEventLoopGroup();
        }

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast("decoder", new NettyDecoder());
                        pipeline.addLast("encoder", new NettyEncoder());
                        pipeline.addLast("handler", new NettyServerChannelAdapter(NettyServer.this));
                    }
                });

        serverBootstrap.option(ChannelOption.SO_BACKLOG, 128);
        serverBootstrap.childOption(ChannelOption.TCP_NODELAY, true);
        serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);

        ChannelFuture future = serverBootstrap.bind(this.address).sync();
        serverChannel = future.channel();
    }

    @Override
    protected void doStop() throws Exception {
        try {
            // close listen socket
            if (serverChannel != null) {
                serverChannel.close();
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
                bossGroup = null;
                workerGroup = null;
                threadPool.shutdownNow();
            }
        } catch (Exception e) {
            LOG.error("NettyServer close Error.", e);
        }
    }

    @Override
    public void handle(ChannelHandlerContext ctx, NettyMessage msg) {
        this.handler.handle(ctx, msg);
    }
}
