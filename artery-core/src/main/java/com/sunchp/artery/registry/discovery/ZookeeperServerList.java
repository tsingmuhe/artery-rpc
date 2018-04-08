package com.sunchp.artery.registry.discovery;

import com.sunchp.artery.registry.ZookeeperInstance;
import com.sunchp.artery.utils.ReflectionUtils;
import org.apache.curator.x.discovery.ServiceCache;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceInstance;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.sunchp.artery.utils.ReflectionUtils.rethrowRuntimeException;

public class ZookeeperServerList<T> implements ServerList<T> {
    private final Class<T> serviceInterface;
    private final ServiceDiscovery<ZookeeperInstance> serviceDiscovery;
    private ServiceCache<ZookeeperInstance> serviceCache;

    public ZookeeperServerList(Class<T> serviceInterface, ServiceDiscovery<ZookeeperInstance> serviceDiscovery) {
        this.serviceInterface = serviceInterface;
        this.serviceDiscovery = serviceDiscovery;
    }

    @Override
    public void watch() {
        String serviceId = getServiceInterface().getName();

        this.serviceCache = getServiceDiscovery().serviceCacheBuilder().name(serviceId).build();

        try {
            serviceCache.start();
        } catch (Exception e) {
            ReflectionUtils.rethrowRuntimeException(e);
        }

        this.serviceCache.addListener(new ServerListWatcherListener(this));
    }

    @Override
    public Class<T> getServiceInterface() {
        return this.serviceInterface;
    }

    @Override
    public ServiceDiscovery<ZookeeperInstance> getServiceDiscovery() {
        return this.serviceDiscovery;
    }

    @Override
    public void onRefresh() {
    }

    @Override
    public List<ZookeeperServer> getInitialListOfServers() {
        return getServers();
    }

    @Override
    public List<ZookeeperServer> getUpdatedListOfServers() {
        return getServers();
    }

    protected List<ZookeeperServer> getServers() {
        String serviceId = getServiceInterface().getName();
        try {
            if (this.serviceDiscovery == null) {
                return Collections.EMPTY_LIST;
            }
            Collection<ServiceInstance<ZookeeperInstance>> instances = this.serviceDiscovery.queryForInstances(serviceId);
            if (instances == null || instances.isEmpty()) {
                return Collections.EMPTY_LIST;
            }
            List<ZookeeperServer> servers = new ArrayList<>();
            for (ServiceInstance<ZookeeperInstance> instance : instances) {
                servers.add(new ZookeeperServer(new InetSocketAddress(instance.getAddress(), instance.getPort())));
            }

            return servers;
        } catch (Exception e) {
            rethrowRuntimeException(e);
        }
        return Collections.EMPTY_LIST;
    }
}
