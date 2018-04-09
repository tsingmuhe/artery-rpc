package com.sunchp.artery.transport.server.handler;


import com.sunchp.artery.transport.server.Server;

public abstract class AbstractHandlerContainer extends AbstractHandler implements HandlerContainer {
    @Override
    public void setServer(Server server) {
        super.setServer(server);
        Handler[] handlers = getHandlers();
        if (handlers != null) {
            for (Handler h : handlers) {
                h.setServer(server);
            }
        }
    }
}
