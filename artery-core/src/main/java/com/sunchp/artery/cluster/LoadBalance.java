package com.sunchp.artery.cluster;

import com.sunchp.artery.rpc.Request;
import com.sunchp.artery.transport.client.netty.NettyClient;

import java.util.List;

public interface LoadBalance {
    NettyClient select(Request request, List<NettyClient> clients);
}
