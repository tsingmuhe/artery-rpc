package com.sunchp.artery.transport.server.handler;

import com.sunchp.artery.utils.component.LifeCycle;

public interface HandlerContainer extends LifeCycle {
    public Handler[] getHandlers();
}
