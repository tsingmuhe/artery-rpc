package com.sunchp.artery.transport;

import com.sunchp.artery.transport.netty.NettyClientBuilder;

public abstract class ClientBuilder<T extends ClientBuilder<T>> {
    public static ClientBuilder<?> forHostAndPort(String host, int port) {
        return NettyClientBuilder.forHostAndPort(host, port);
    }

    public abstract Client build();
}
