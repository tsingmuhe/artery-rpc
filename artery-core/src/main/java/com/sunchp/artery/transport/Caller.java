package com.sunchp.artery.transport;

import com.sunchp.artery.rpc.Request;
import com.sunchp.artery.rpc.ResponsePromise;

public interface Caller {
    ResponsePromise call(Request request) throws TransportException;
}
