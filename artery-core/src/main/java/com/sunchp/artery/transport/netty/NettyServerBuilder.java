package com.sunchp.artery.transport.netty;

import com.sunchp.artery.rpc.Exporter;
import com.sunchp.artery.rpc.ExporterRegistry;
import com.sunchp.artery.transport.Server;
import com.sunchp.artery.transport.ServerBuilder;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class NettyServerBuilder extends ServerBuilder<NettyServerBuilder> {

    private final SocketAddress address;

    private final ExporterRegistry registry = new ExporterRegistry();


    public static ServerBuilder<?> forPort(int port) {
        return new NettyServerBuilder(port);
    }

    public NettyServerBuilder(int port) {
        this.address = new InetSocketAddress(port);
    }

    @Override
    public NettyServerBuilder addService(Exporter service) {
        registry.addExporter(service);
        return this;
    }

    @Override
    public Server build() {
        return new NettyServer(address, registry);
    }
}
