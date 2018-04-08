package com.sunchp.artery.transport.netty;

import com.sunchp.artery.transport.EndpointState;
import com.sunchp.artery.rpc.ExporterRegistry;
import com.sunchp.artery.transport.AbstractServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketAddress;

import static io.netty.channel.ChannelOption.SO_BACKLOG;

public class NettyServer extends AbstractServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(NettyServer.class);

    private final SocketAddress address;
    private final ExporterRegistry exporterRegistry;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private Channel serverChannel;


    public NettyServer(SocketAddress address, ExporterRegistry exporterRegistry) {
        this.address = address;
        this.exporterRegistry = exporterRegistry;
    }

    @Override
    public synchronized void start() throws IOException {
        if (isAvailable()) {
            LOGGER.warn("NettyServer already start.");
            return;
        }

        if (bossGroup == null) {
            bossGroup = new NioEventLoopGroup(1);
            workerGroup = new NioEventLoopGroup();
        }

        LOGGER.info("NettyServer start Open.");
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast("decoder", new NettyDecoder());
                        pipeline.addLast("encoder", new NettyEncoder());
                        pipeline.addLast("handler", new NettyServerChannelHandler(exporterRegistry));
                    }
                });

        serverBootstrap.option(SO_BACKLOG, 128);
        serverBootstrap.childOption(ChannelOption.TCP_NODELAY, true);
        serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);

        ChannelFuture future = serverBootstrap.bind(address);
        try {
            future.await();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted waiting for bind");
        }
        if (!future.isSuccess()) {
            throw new IOException("Failed to bind", future.cause());
        }

        serverChannel = future.channel();
        state = EndpointState.ALIVE;
        LOGGER.info("NettyServer ServerChannel finish Open.");
    }


    @Override
    public synchronized void shutdown() {
        if (state.isCloseState()) {
            LOGGER.info("NettyServer close fail: already close.");
            return;
        }

        if (state.isUnInitState()) {
            LOGGER.info("NettyServer close Fail: don't need to close because node is unInit state.");
            return;
        }

        try {
            // close listen socket
            if (serverChannel != null) {
                serverChannel.close();
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
                bossGroup = null;
                workerGroup = null;
            }
            // 设置close状态
            state = EndpointState.CLOSE;
            LOGGER.info("NettyServer close Success.");
        } catch (Exception e) {
            LOGGER.error("NettyServer close Error.", e);
        }
    }


    @Override
    public int getPort() {
        return 0;
    }
}
