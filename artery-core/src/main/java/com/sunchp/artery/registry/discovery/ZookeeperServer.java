package com.sunchp.artery.registry.discovery;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class ZookeeperServer {
    private InetSocketAddress address;

    public ZookeeperServer(InetSocketAddress address) {
        this.address = address;
    }

    public InetSocketAddress getAddress() {
        return address;
    }
}
