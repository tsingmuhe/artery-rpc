package com.sunchp.artery.transport.client;

import com.sunchp.artery.utils.Promise;

public interface Destination {
    String getHost();

    int getPort();

    void newConnection(Promise<Connection> promise);
}
