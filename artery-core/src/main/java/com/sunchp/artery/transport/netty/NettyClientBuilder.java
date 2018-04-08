package com.sunchp.artery.transport.netty;

import com.sunchp.artery.transport.Client;
import com.sunchp.artery.transport.ClientBuilder;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class NettyClientBuilder extends ClientBuilder<NettyClientBuilder> {
    private final SocketAddress remoteAddress;
    private int connections;

    public static ClientBuilder<?> forHostAndPort(String host, int port) {
        return new NettyClientBuilder(host, port);
    }

    public NettyClientBuilder(String host, int port) {
        this.remoteAddress = new InetSocketAddress(host, port);
    }

    public NettyClientBuilder connections(int value) {
        this.connections = value;
        return this;
    }

    @Override
    public Client build() {
        return new NettyClient(remoteAddress, connections);
    }
}
