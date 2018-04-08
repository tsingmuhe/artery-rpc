package com.sunchp.artery.registry.serviceregistry;

import com.sunchp.artery.registry.ZookeeperInstance;
import com.sunchp.artery.utils.ReflectionUtils;
import org.apache.curator.x.discovery.ServiceDiscovery;

public class ZookeeperServiceRegistry {
    private final ServiceDiscovery<ZookeeperInstance> serviceDiscovery;

    public ZookeeperServiceRegistry(ServiceDiscovery<ZookeeperInstance> serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    public void register(ZookeeperRegistration registration) {
        try {
            getServiceDiscovery().registerService(registration.getServiceInstance());
        } catch (Exception e) {
            ReflectionUtils.rethrowRuntimeException(e);
        }
    }

    public void deregister(ZookeeperRegistration registration) {
        try {
            getServiceDiscovery().unregisterService(registration.getServiceInstance());
        } catch (Exception e) {
            ReflectionUtils.rethrowRuntimeException(e);
        }
    }

    private ServiceDiscovery<ZookeeperInstance> getServiceDiscovery() {
        return this.serviceDiscovery;
    }
}
