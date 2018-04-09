package com.sunchp.artery.cluster;

import com.sunchp.artery.registry.discovery.ServerList;
import com.sunchp.artery.rpc.Request;
import com.sunchp.artery.rpc.ResponsePromise;
import com.sunchp.artery.transport.client.Client;

import java.util.List;

public interface Cluster<T> extends ServerList<T> {
    void init();

    LoadBalance getLoadBalance();

    HaStrategy getHaStrategy();

    List<Client> getClients();

    ResponsePromise send(Request request);
}
