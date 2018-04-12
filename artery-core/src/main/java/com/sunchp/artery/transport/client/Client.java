package com.sunchp.artery.transport.client;

import com.sunchp.artery.rpc.Request;
import com.sunchp.artery.rpc.ResponsePromise;
import com.sunchp.artery.utils.component.LifeCycle;

public interface Client extends LifeCycle {
    ResponsePromise send(Request request);
}
