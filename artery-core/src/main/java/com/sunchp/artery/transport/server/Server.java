package com.sunchp.artery.transport.server;

import com.sunchp.artery.rpc.Request;
import com.sunchp.artery.rpc.Response;
import com.sunchp.artery.transport.codec.NettyDecoder;
import com.sunchp.artery.transport.codec.NettyEncoder;
import com.sunchp.artery.transport.server.handler.Handler;
import com.sunchp.artery.transport.server.handler.HandlerWrapper;
import com.sunchp.artery.utils.QueuedThreadPool;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class Server extends HandlerWrapper {
    private static final Logger LOG = LoggerFactory.getLogger(Server.class);

    private final QueuedThreadPool _threadPool;
    private final InetSocketAddress addr;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private Channel serverChannel;

    public Server(InetSocketAddress addr) {
        this(addr, (QueuedThreadPool) null);
    }

    public Server(InetSocketAddress addr, QueuedThreadPool pool) {
        this.addr = addr;
        _threadPool = pool != null ? pool : new QueuedThreadPool();
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
                        pipeline.addLast("handler", new NettyServerChannelHandler(Server.this));
                    }
                });

        serverBootstrap.option(ChannelOption.SO_BACKLOG, 128);
        serverBootstrap.childOption(ChannelOption.TCP_NODELAY, true);
        serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);

        ChannelFuture future = serverBootstrap.bind(addr).sync();
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
            }
        } catch (Exception e) {
            LOG.error("NettyServer close Error.", e);
        }
    }

    @Override
    public void handle(Request request, Response response) {
        Handler handler = _handler;
        if (handler != null) {
            _threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    handler.handle(request, response);
                }
            });
        }
    }

    @Override
    public Handler[] getHandlers() {
        return new Handler[0];
    }
}
