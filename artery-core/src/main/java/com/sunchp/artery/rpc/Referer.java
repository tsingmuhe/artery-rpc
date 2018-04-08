package com.sunchp.artery.rpc;

import com.sunchp.artery.cluster.Cluster;

public interface Referer<T> {
    Class<T> getServiceInterface();

    T getProxy();

    Cluster<T> getCluster();
}
