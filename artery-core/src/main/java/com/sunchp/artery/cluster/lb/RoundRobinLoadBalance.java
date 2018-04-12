package com.sunchp.artery.cluster.lb;

import com.sunchp.artery.cluster.LoadBalance;
import com.sunchp.artery.rpc.Request;
import com.sunchp.artery.transport.client.netty.NettyClient;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundRobinLoadBalance implements LoadBalance {
    private final AtomicInteger index = new AtomicInteger(0);

    @Override
    public NettyClient select(Request request, List<NettyClient> clients) {
        if (clients == null || clients.size() == 0) {
            return null;
        }

        int thisIndex = Math.abs(index.getAndIncrement());
        return clients.get(thisIndex % clients.size());
    }
}
