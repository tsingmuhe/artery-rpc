package com.sunchp.artery.springsupport;

import com.sunchp.artery.transport.server.Server;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.net.InetSocketAddress;

public class NettyApplicationContext extends AnnotationConfigApplicationContext {
    private Server server;

    public NettyApplicationContext() {
    }

    public NettyApplicationContext(DefaultListableBeanFactory beanFactory) {
        super(beanFactory);
    }

    public NettyApplicationContext(Class<?>... annotatedClasses) {
        super(annotatedClasses);
    }

    public NettyApplicationContext(String... basePackages) {
        super(basePackages);
    }

    @Override
    public final void refresh() throws BeansException, IllegalStateException {
        try {
            super.refresh();
        } catch (RuntimeException ex) {
            stopAndReleaseNettyServer();
            throw ex;
        }
    }

    @Override
    protected void onRefresh() {
        super.onRefresh();
        try {
            createNettyServer();
        } catch (Throwable ex) {
            throw new ApplicationContextException("Unable to start embedded container",
                    ex);
        }
    }

    @Override
    protected void finishRefresh() {
        super.finishRefresh();
        Server nettyServer = startEmbeddedServletContainer();
        if (nettyServer != null) {
            publishEvent(new NettyServerInitializedEvent(nettyServer));
        }
    }

    @Override
    protected void onClose() {
        super.onClose();
        stopAndReleaseNettyServer();
    }


    private void createNettyServer() {
        this.server = new Server(new InetSocketAddress("127.0.0.1", 8080));
    }

    private Server startEmbeddedServletContainer() {
        Server nettyServer = this.server;

        if (nettyServer != null) {
            try {
                nettyServer.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return nettyServer;
    }

    private void stopAndReleaseNettyServer() {
        Server nettyServer = this.server;
        if (nettyServer != null) {
            try {
                nettyServer.stop();
                this.server = null;
            } catch (Exception ex) {
                throw new IllegalStateException(ex);
            }
        }
    }


}
