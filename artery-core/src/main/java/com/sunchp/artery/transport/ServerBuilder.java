package com.sunchp.artery.transport;

import com.sunchp.artery.rpc.Exporter;
import com.sunchp.artery.transport.netty.NettyServerBuilder;

public abstract class ServerBuilder<T extends ServerBuilder<T>> {
    public static ServerBuilder<?> forPort(int port) {
        return NettyServerBuilder.forPort(port);
    }

    public abstract T addService(Exporter service);

    public abstract Server build();
}
