package com.sunchp.artery.cluster;

import com.sunchp.artery.rpc.Request;
import com.sunchp.artery.transport.Client;

import java.util.List;

public interface LoadBalance {
    Client select(Request request, List<Client> clients);
}
