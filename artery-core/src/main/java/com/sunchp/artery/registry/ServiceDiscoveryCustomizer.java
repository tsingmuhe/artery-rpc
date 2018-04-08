package com.sunchp.artery.registry;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.details.InstanceSerializer;

public class ServiceDiscoveryCustomizer {
    protected CuratorFramework curator;

    protected ZookeeperDiscoveryProperties properties;

    protected InstanceSerializer<ZookeeperInstance> instanceSerializer;

    public ServiceDiscoveryCustomizer(CuratorFramework curator, ZookeeperDiscoveryProperties properties, InstanceSerializer<ZookeeperInstance> instanceSerializer) {
        this.curator = curator;
        this.properties = properties;
        this.instanceSerializer = instanceSerializer;
    }

    public ServiceDiscovery<ZookeeperInstance> customize(ServiceDiscoveryBuilder<ZookeeperInstance> builder) {
        return builder
                .client(this.curator)
                .basePath(this.properties.getRoot())
                .serializer(this.instanceSerializer)
                .build();
    }
}
