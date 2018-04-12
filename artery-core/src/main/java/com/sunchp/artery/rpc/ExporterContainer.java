package com.sunchp.artery.rpc;

public interface ExporterContainer {
    void addExporter(Exporter<?> exporter);

    Exporter<?> get(String className);
}
