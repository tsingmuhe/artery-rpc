package com.sunchp.artery.rpc;

import com.sunchp.artery.utils.FuturePromise;

public class ResponsePromise extends FuturePromise {
    private Request request;

    public ResponsePromise(Request request) {
        this.request = request;
    }

    public Request getRequest() {
        return request;
    }

}
