package com.sunchp.artery.transport.client;

import com.sunchp.artery.transport.codec.NettyMessage;
import com.sunchp.artery.utils.Promise;
import com.sunchp.artery.utils.component.LifeCycle;

import java.net.InetSocketAddress;

public interface Destination extends LifeCycle {
    InetSocketAddress getRemoteAddress();

    Connection newConnection();

    public void processMessage(NettyMessage msg);
}
