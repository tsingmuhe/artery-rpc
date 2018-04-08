package com.sunchp.artery.transport;

import java.net.SocketAddress;

public abstract class AbstractClient extends AbstractEndpoint implements Client {
    protected final SocketAddress remoteAddress;

    public AbstractClient(SocketAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    @Override
    public SocketAddress getRemoteAddress() {
        return remoteAddress;
    }
}
