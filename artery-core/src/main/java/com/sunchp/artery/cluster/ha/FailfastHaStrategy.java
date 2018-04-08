package com.sunchp.artery.cluster.ha;

import com.sunchp.artery.cluster.HaStrategy;
import com.sunchp.artery.cluster.LoadBalance;
import com.sunchp.artery.rpc.Request;
import com.sunchp.artery.rpc.ResponsePromise;
import com.sunchp.artery.transport.Client;

import java.util.List;

public class FailfastHaStrategy implements HaStrategy {
    @Override
    public ResponsePromise call(Request request, LoadBalance loadBalance, List<Client> clients) {
        Client client = loadBalance.select(request, clients);
        return client.call(request);
    }
}
