package com.sunchp.artery.transport;

import java.net.SocketAddress;

public interface Client extends Caller, Endpoint {
    SocketAddress getRemoteAddress();
}
