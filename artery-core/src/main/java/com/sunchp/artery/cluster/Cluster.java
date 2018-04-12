package com.sunchp.artery.cluster;

import com.sunchp.artery.registry.discovery.ServerList;
import com.sunchp.artery.rpc.Request;
import com.sunchp.artery.rpc.ResponsePromise;
import com.sunchp.artery.transport.client.netty.NettyClient;

import java.util.List;

public interface Cluster<T> extends ServerList<T> {
    LoadBalance getLoadBalance();

    HaStrategy getHaStrategy();

    List<NettyClient> getClients();

    ResponsePromise send(Request request);
}
