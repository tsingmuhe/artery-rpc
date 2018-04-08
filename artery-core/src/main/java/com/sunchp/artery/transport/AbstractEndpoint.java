package com.sunchp.artery.transport;

public abstract class AbstractEndpoint implements Endpoint {
    protected volatile EndpointState state = EndpointState.UNINIT;

    @Override
    public boolean isAvailable() {
        return state.isAliveState();
    }

    @Override
    public boolean isShutdown() {
        return state.isCloseState();
    }
}
