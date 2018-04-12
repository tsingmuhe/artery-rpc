package com.sunchp.artery.springsupport.context;

import com.sunchp.artery.transport.server.NettyServer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.StringUtils;

public class NettyApplicationContext extends AnnotationConfigApplicationContext {
    private NettyServer server;

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
            throw new ApplicationContextException("Unable to start embedded container", ex);
        }
    }

    @Override
    protected void finishRefresh() {
        super.finishRefresh();
        startNettyServer();
    }

    @Override
    protected void onClose() {
        super.onClose();
        stopAndReleaseNettyServer();
    }

    protected NettyServerFactory getNettyServerFactory() {
        // Use bean names so that we don't consider the hierarchy
        String[] beanNames = getBeanFactory().getBeanNamesForType(NettyServerFactory.class);
        if (beanNames.length == 0) {
            throw new ApplicationContextException(
                    "Unable to start NettyApplicationContext due to missing "
                            + "NettyServerFactory bean.");
        }
        if (beanNames.length > 1) {
            throw new ApplicationContextException(
                    "Unable to start NettyApplicationContext due to multiple "
                            + "NettyServerFactory beans : "
                            + StringUtils.arrayToCommaDelimitedString(beanNames));
        }
        return getBeanFactory().getBean(beanNames[0], NettyServerFactory.class);
    }


    private void createNettyServer() {
        this.server = getNettyServerFactory().getServer();
    }


    private NettyServer startNettyServer() {
        if (this.server == null) {
            return null;
        }

        try {
            this.server.start();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return this.server;
    }

    private void stopAndReleaseNettyServer() {
        if (this.server == null) {
            return;
        }

        try {
            this.server.stop();
            this.server = null;
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }
}
