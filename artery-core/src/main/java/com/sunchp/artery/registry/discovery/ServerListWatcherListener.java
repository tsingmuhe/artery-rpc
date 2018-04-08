package com.sunchp.artery.registry.discovery;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.x.discovery.details.ServiceCacheListener;

public class ServerListWatcherListener implements ServiceCacheListener {
    private final ServerList serverList;

    public ServerListWatcherListener(ServerList serverList) {
        this.serverList = serverList;
    }

    @Override
    public void cacheChanged() {
        this.serverList.onRefresh();
    }

    @Override
    public void stateChanged(CuratorFramework client, ConnectionState newState) {

    }
}
