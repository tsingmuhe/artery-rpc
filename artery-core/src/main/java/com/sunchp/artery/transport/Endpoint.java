package com.sunchp.artery.transport;

import java.io.IOException;

public interface Endpoint {
    void start() throws IOException;

    boolean isAvailable();

    void shutdown();

    boolean isShutdown();
}
