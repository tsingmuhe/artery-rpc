package com.sunchp.artery.transport.server.handler;

import com.sunchp.artery.exporter.Invocation;
import com.sunchp.artery.exporter.InvocationResult;
import com.sunchp.artery.rpc.Exporter;
import com.sunchp.artery.rpc.ExporterContainer;
import com.sunchp.artery.rpc.Request;
import com.sunchp.artery.rpc.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExporterContainerHandler extends AbstractHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExporterContainerHandler.class);

    private final ExporterContainer exporterContainer;

    public ExporterContainerHandler(ExporterContainer exporterContainer) {
        this.exporterContainer = exporterContainer;
    }

    @Override
    protected void processMessage(Request request, Response response) {
        try {
            Exporter exporter = readExporter(request);
            Invocation invocation = readInvocation(request);
            InvocationResult result = exporter.invokeAndCreateResult(invocation);
            writeRemoteInvocationResult(request, response, result);
        } catch (Exception e) {
            LOGGER.error("", e);
            response.setException(e);
        }
    }

    protected Exporter<?> readExporter(Request request) {
        return exporterContainer.get(request.getClassName());
    }

    protected Invocation readInvocation(Request request) {
        return new Invocation(request.getMethodName(), request.getParameterTypes(), request.getParameters());
    }

    protected void writeRemoteInvocationResult(Request request, Response response, InvocationResult result) {
        if (result.hasException()) {
            response.setException(result.getException());
            return;
        }

        response.setValue(result.getValue());
    }
}
