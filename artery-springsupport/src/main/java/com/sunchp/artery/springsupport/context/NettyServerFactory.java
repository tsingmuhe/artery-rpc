package com.sunchp.artery.springsupport.context;

import com.sunchp.artery.transport.server.NettyServer;
import com.sunchp.artery.transport.server.handler.Handler;
import com.sunchp.artery.utils.QueuedThreadPool;

import java.net.InetSocketAddress;

public class NettyServerFactory {
    private InetSocketAddress address;
    private QueuedThreadPool threadPool;
    private Handler handler;

    public InetSocketAddress getAddress() {
        return address;
    }

    public void setAddress(InetSocketAddress address) {
        this.address = address;
    }

    public QueuedThreadPool getThreadPool() {
        return threadPool;
    }

    public void setThreadPool(QueuedThreadPool threadPool) {
        this.threadPool = threadPool;
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public NettyServer getServer() {
        if (address == null) {
            throw new RuntimeException("Netty server address must be set");
        }

        return new NettyServer(address, threadPool, handler);
    }
}
