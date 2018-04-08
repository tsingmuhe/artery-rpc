package com.sunchp.artery.registry.discovery;

import java.net.SocketAddress;

public class ZookeeperServer {
    private SocketAddress address;

    public ZookeeperServer(SocketAddress address) {
        this.address = address;
    }

    public SocketAddress getAddress() {
        return address;
    }
}
