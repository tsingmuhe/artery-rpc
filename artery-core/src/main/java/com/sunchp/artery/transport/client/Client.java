package com.sunchp.artery.transport.client;

import com.sunchp.artery.rpc.Request;
import com.sunchp.artery.rpc.ResponsePromise;
import com.sunchp.artery.utils.component.AbstractLifeCycle;

public class Client extends AbstractLifeCycle {
    private NettyDestination nettyDestination;
    private NettyConnection nettyConnection;

    public Client(String host, int port) {
        this.nettyDestination = new NettyDestination(host, port);
    }

    protected void doStart() throws Exception {
        this.nettyDestination.start();
        this.nettyConnection = this.nettyDestination.newConnection();
    }

    protected void doStop() throws Exception {
        this.nettyDestination.stop();
        this.nettyConnection.close();
    }

    public ResponsePromise send(Request request) {
        return this.nettyConnection.send(request);
    }
}
