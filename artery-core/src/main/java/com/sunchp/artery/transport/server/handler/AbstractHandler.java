package com.sunchp.artery.transport.server.handler;


import com.sunchp.artery.transport.server.Server;
import com.sunchp.artery.utils.component.AbstractLifeCycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractHandler extends AbstractLifeCycle implements Handler {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractHandler.class);

    private Server _server;

    @Override
    protected void doStart() throws Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("starting {}", this);
        }
        if (_server == null) {
            LOG.warn("No Server set for {}", this);
        }
        super.doStart();
    }

    @Override
    protected void doStop() throws Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("stopping {}", this);
        }
        super.doStop();
    }

    @Override
    public void setServer(Server server) {
        if (_server == server) {
            return;
        }
        if (isStarted()) {
            throw new IllegalStateException(STARTED);
        }
        _server = server;
    }

    @Override
    public Server getServer() {
        return _server;
    }

    @Override
    public void destroy() {
        if (!isStopped()) {
            throw new IllegalStateException("!STOPPED");
        }
    }
}
