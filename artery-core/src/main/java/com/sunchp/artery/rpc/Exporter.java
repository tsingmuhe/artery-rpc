package com.sunchp.artery.rpc;

import com.sunchp.artery.exporter.Invocation;
import com.sunchp.artery.exporter.InvocationResult;

public interface Exporter<T> {
    Class<T> getServiceInterface();

    T getService();

    String getName();

    InvocationResult invokeAndCreateResult(Invocation invocation);
}
