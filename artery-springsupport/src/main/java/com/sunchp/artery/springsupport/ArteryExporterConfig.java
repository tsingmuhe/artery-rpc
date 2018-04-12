package com.sunchp.artery.springsupport;

import com.sunchp.artery.exporter.DefaultExporter;
import com.sunchp.artery.registry.ZookeeperInstance;
import com.sunchp.artery.registry.serviceregistry.ZookeeperRegistration;
import com.sunchp.artery.registry.serviceregistry.ZookeeperServiceRegistry;
import com.sunchp.artery.rpc.Exporter;
import com.sunchp.artery.rpc.ExporterContainer;
import com.sunchp.artery.springsupport.annotation.ArteryService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ArteryExporterConfig implements ApplicationContextAware {
    private ApplicationContext applicationContext;
    private ExporterContainer exporterContainer;
    private ZookeeperServiceRegistry zookeeperServiceRegistry;
    private String host;
    private int port;

    private List<Exporter> exporters = new ArrayList<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void setExporterContainer(ExporterContainer exporterContainer) {
        this.exporterContainer = exporterContainer;
    }

    public void setZookeeperServiceRegistry(ZookeeperServiceRegistry zookeeperServiceRegistry) {
        this.zookeeperServiceRegistry = zookeeperServiceRegistry;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @PostConstruct
    public void afterPropertiesSet() throws Exception {
        Map<String, Object> serviceBeanMap = this.applicationContext.getBeansWithAnnotation(ArteryService.class);
        if (serviceBeanMap == null || serviceBeanMap.isEmpty()) {
            return;
        }

        for (Object serviceBean : serviceBeanMap.values()) {
            Class<?> interfaceClass = serviceBean.getClass().getAnnotation(ArteryService.class).interfaceClass();
            if (void.class.equals(interfaceClass)) {
                continue;
            }

            Exporter<?> exporter = new DefaultExporter<>(interfaceClass, serviceBean);
            exporters.add(exporter);
        }
        for (Exporter exporter : exporters) {
            exporterContainer.addExporter(exporter);
            ZookeeperRegistration registration = ZookeeperRegistration.builder().address(this.host).port(this.port).name(exporter.getName()).payload(new ZookeeperInstance()).build();
            zookeeperServiceRegistry.register(registration);
        }
    }

    public List<Exporter> getExporters() {
        return exporters;
    }
}
