package com.sunchp.artery.springsupport;

import com.sunchp.artery.proxy.jdk.JdkProxyFactory;
import com.sunchp.artery.referer.DefaultReferer;
import com.sunchp.artery.rpc.Referer;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.springframework.beans.factory.config.AbstractFactoryBean;

public class ArteryRefererFactoryBean<T> extends AbstractFactoryBean<T> {

    private final Class<T> serviceInterface;
    private final ServiceDiscovery serviceDiscovery;

    public ArteryRefererFactoryBean(Class<T> serviceInterface, ServiceDiscovery serviceDiscovery) {
        this.serviceInterface = serviceInterface;
        this.serviceDiscovery = serviceDiscovery;
    }

    @Override
    public Class<T> getObjectType() {
        return serviceInterface;
    }

    @Override
    protected T createInstance() throws Exception {
        Referer<T> referer = new DefaultReferer<T>(serviceInterface, new JdkProxyFactory(), serviceDiscovery);
        return referer.getProxy();
    }
}
