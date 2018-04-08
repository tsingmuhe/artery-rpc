package com.sunchp.artery.transport;

public abstract class AbstractServer extends AbstractEndpoint implements Server {
    public int getPort() {
        return -1;
    }
}
