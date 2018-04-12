package com.sunchp.artery.exporter;

import com.sunchp.artery.rpc.Exporter;
import com.sunchp.artery.rpc.ExporterContainer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultExporterContainer implements ExporterContainer {
    private final Map<String, Exporter<?>> map = new ConcurrentHashMap<>();

    @Override
    public void addExporter(Exporter<?> exporter) {
        if (map.containsKey(exporter.getName())) {
            throw new RuntimeException("exporter name already exist.name=" + exporter.getName());
        }

        map.put(exporter.getName(), exporter);
    }

    @Override
    public Exporter<?> get(String className) {
        return map.get(className);
    }
}
