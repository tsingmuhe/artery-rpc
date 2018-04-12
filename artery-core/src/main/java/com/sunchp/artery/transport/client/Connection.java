package com.sunchp.artery.transport.client;

import com.sunchp.artery.rpc.Request;
import com.sunchp.artery.rpc.ResponsePromise;

import java.io.Closeable;

public interface Connection extends Closeable {
    ResponsePromise send(Request request);
}
