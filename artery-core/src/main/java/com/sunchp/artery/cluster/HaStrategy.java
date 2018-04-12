package com.sunchp.artery.cluster;

import com.sunchp.artery.rpc.Request;
import com.sunchp.artery.rpc.ResponsePromise;
import com.sunchp.artery.transport.client.netty.NettyClient;

import java.util.List;

public interface HaStrategy {
     ResponsePromise call(Request request, LoadBalance loadBalance, List<NettyClient> clients);
}
