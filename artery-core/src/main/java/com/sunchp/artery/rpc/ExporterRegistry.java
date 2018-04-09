package com.sunchp.artery.rpc;

import com.sunchp.artery.exporter.Invocation;
import com.sunchp.artery.exporter.InvocationResult;
import com.sunchp.artery.transport.server.handler.AbstractHandler;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ExporterRegistry extends AbstractHandler {
    private final ConcurrentMap<String, Exporter<?>> map = new ConcurrentHashMap();

    public void addExporter(Exporter<?> exporter) {
        if (map.containsKey(exporter.getName())) {
            throw new RuntimeException("exporter name already exist.name=" + exporter.getName());
        }

        map.put(exporter.getName(), exporter);
    }

    @Override
    public void handle(Request request, Response response) {
        Exporter exporter = readExporter(request);
        Invocation invocation = readInvocation(request);
        InvocationResult result = exporter.invokeAndCreateResult(invocation);
        writeRemoteInvocationResult(request, response, result);
    }

    private Exporter<?> readExporter(Request request) {
        return map.get(request.getClassName());
    }

    private Invocation readInvocation(Request request) {
        return new Invocation(request.getMethodName(), request.getParameterTypes(), request.getParameters());
    }

    private void writeRemoteInvocationResult(Request request, Response response, InvocationResult result) {
        if (result.hasException()) {
            response.setException(result.getException());
            return;
        }

        response.setValue(result.getValue());
    }
}
