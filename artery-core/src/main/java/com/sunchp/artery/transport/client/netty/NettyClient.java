package com.sunchp.artery.transport.client.netty;

import com.sunchp.artery.rpc.Request;
import com.sunchp.artery.rpc.ResponsePromise;
import com.sunchp.artery.transport.client.Client;
import com.sunchp.artery.transport.client.Connection;
import com.sunchp.artery.utils.component.AbstractLifeCycle;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class NettyClient extends AbstractLifeCycle implements Client {
    private final AtomicInteger index = new AtomicInteger(0);

    private NettyDestination destination;
    private ArrayList<NettyConnection> connections;
    private int nConnections;

    public NettyClient(InetSocketAddress address) {
        this(address, 10);
    }

    public NettyClient(InetSocketAddress address, int nConnections) {
        this.destination = new NettyDestination(address);
        this.nConnections = nConnections;
    }

    protected void doStart() throws Exception {
        this.destination.start();
        this.connections = new ArrayList<>();

        for (int i = 0; i < nConnections; i++) {
            NettyConnection connection = (NettyConnection) this.destination.newConnection();
            if (connection != null) {
                this.connections.add(connection);
            }
        }
    }

    protected void doStop() throws Exception {
        this.destination.stop();
        for (Connection connection : connections) {
            connection.close();
        }
    }

    @Override
    public ResponsePromise send(Request request) {
        Connection connection = selectConnetion();
        if (connection != null) {
            return connection.send(request);
        }

        throw new RuntimeException("no availabe connection");
    }

    public Connection selectConnetion() {
        if (connections == null || connections.size() == 0) {
            return null;
        }

        int thisIndex = Math.abs(index.getAndIncrement());
        return connections.get(thisIndex % connections.size());
    }
}
