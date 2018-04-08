package com.sunchp.artery.referer;

import com.sunchp.artery.cluster.Cluster;
import com.sunchp.artery.cluster.DefaultCluster;
import com.sunchp.artery.cluster.ha.FailfastHaStrategy;
import com.sunchp.artery.cluster.lb.RandomLoadBalance;
import com.sunchp.artery.proxy.ProxyFactory;
import com.sunchp.artery.proxy.jdk.RefererInvocationHandler;
import com.sunchp.artery.registry.ZookeeperInstance;
import com.sunchp.artery.rpc.Referer;
import org.apache.curator.x.discovery.ServiceDiscovery;

public class DefaultReferer<T> implements Referer<T> {
    private final Class<T> serviceInterface;
    private final ProxyFactory proxyFactory;
    private final ServiceDiscovery<ZookeeperInstance> serviceDiscovery;

    private boolean initialized = false;
    private Cluster<T> cluster;
    private T proxy;

    public DefaultReferer(Class<T> serviceInterface, ProxyFactory proxyFactory, ServiceDiscovery<ZookeeperInstance> serviceDiscovery) {
        this.serviceInterface = serviceInterface;
        this.proxyFactory = proxyFactory;
        this.serviceDiscovery = serviceDiscovery;
    }

    @Override
    public Class<T> getServiceInterface() {
        return this.serviceInterface;
    }

    @Override
    public T getProxy() {
        if (proxy == null) {
            initProxy();
        }
        return this.proxy;
    }

    @Override
    public Cluster<T> getCluster() {
        return this.cluster;
    }

    private synchronized void initProxy() {
        if (initialized) {
            return;
        }

        this.cluster = new DefaultCluster(this.serviceInterface, serviceDiscovery, new FailfastHaStrategy(), new RandomLoadBalance());
        this.cluster.init();
        this.cluster.watch();
        this.proxy = this.proxyFactory.getProxy(this.serviceInterface, new RefererInvocationHandler(cluster));
        initialized = true;
    }
}
