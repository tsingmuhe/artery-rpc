package com.sunchp.artery.cluster;

import com.sunchp.artery.registry.discovery.ServerList;
import com.sunchp.artery.transport.Caller;
import com.sunchp.artery.transport.Client;

import java.util.List;

public interface Cluster<T> extends Caller, ServerList<T> {
    void init();

    LoadBalance getLoadBalance();

    HaStrategy getHaStrategy();

    List<Client> getClients();

}
