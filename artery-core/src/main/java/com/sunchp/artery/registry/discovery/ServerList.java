package com.sunchp.artery.registry.discovery;

import com.sunchp.artery.registry.ZookeeperInstance;
import org.apache.curator.x.discovery.ServiceDiscovery;

import java.util.List;

public interface ServerList<T> {
    Class<T> getServiceInterface();

    ServiceDiscovery<ZookeeperInstance> getServiceDiscovery();

    void watch();

    void onRefresh();

    List<ZookeeperServer> getInitialListOfServers();

    List<ZookeeperServer> getUpdatedListOfServers();
}
