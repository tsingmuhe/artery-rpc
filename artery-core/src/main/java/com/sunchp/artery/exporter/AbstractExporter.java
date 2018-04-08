package com.sunchp.artery.exporter;

import com.sunchp.artery.rpc.Exporter;

public abstract class AbstractExporter<T> implements Exporter<T> {
    private String name;
    private Class<T> serviceInterface;
    private T service;

    public AbstractExporter(Class<T> serviceInterface, T service) {
        this.name = serviceInterface.getName();
        this.serviceInterface = serviceInterface;
        this.service = service;
    }

    @Override
    public Class<T> getServiceInterface() {
        return serviceInterface;
    }

    @Override
    public T getService() {
        return service;
    }

    @Override
    public String getName() {
        return name;
    }
}
