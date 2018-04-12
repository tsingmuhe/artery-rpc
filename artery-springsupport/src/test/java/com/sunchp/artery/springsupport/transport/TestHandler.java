package com.sunchp.artery.springsupport.transport;

import com.sunchp.artery.rpc.Request;
import com.sunchp.artery.rpc.Response;
import com.sunchp.artery.transport.server.handler.AbstractHandler;

public class TestHandler extends AbstractHandler {
    @Override
    protected void processMessage(Request request, Response response) {
        System.out.println("###########RequestId:" + request.getRequestId());
        response.setValue("hello hello");
    }
}
