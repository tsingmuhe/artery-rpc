package com.sunchp.artery.proxy.jdk;

import com.google.common.reflect.AbstractInvocationHandler;
import com.sunchp.artery.cluster.Cluster;
import com.sunchp.artery.rpc.Request;
import com.sunchp.artery.rpc.ResponsePromise;

import java.lang.reflect.Method;
import java.util.UUID;

public class RefererInvocationHandler<T> extends AbstractInvocationHandler {
    private final Cluster<T> cluster;

    public RefererInvocationHandler(Cluster<T> cluster) {
        this.cluster = cluster;
    }

    @Override
    protected Object handleInvocation(Object o, Method method, Object[] args) throws Throwable {
        Request request = new Request();
        request.setRequestId(UUID.randomUUID().toString());
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setParameters(args);

        ResponsePromise responsePromise = cluster.send(request);
        return responsePromise.get();
    }
}
