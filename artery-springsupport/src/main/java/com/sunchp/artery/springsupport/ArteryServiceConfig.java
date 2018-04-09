package com.sunchp.artery.springsupport;

import com.sunchp.artery.exporter.DefaultExporter;
import com.sunchp.artery.rpc.Exporter;
import com.sunchp.artery.springsupport.annotation.ArteryService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ArteryServiceConfig implements ApplicationContextAware {
    private ApplicationContext applicationContext;
    private List<Exporter> exporters = new ArrayList<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
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
    }

    public List<Exporter> getExporters() {
        return exporters;
    }
}
