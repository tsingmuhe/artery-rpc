package com.sunchp.artery.cluster;

import com.sunchp.artery.registry.ZookeeperInstance;
import com.sunchp.artery.registry.discovery.ZookeeperServer;
import com.sunchp.artery.registry.discovery.ZookeeperServerList;
import com.sunchp.artery.rpc.Request;
import com.sunchp.artery.rpc.ResponsePromise;
import com.sunchp.artery.transport.TransportException;
import com.sunchp.artery.transport.client.netty.NettyClient;
import org.apache.curator.x.discovery.ServiceDiscovery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.sunchp.artery.utils.ReflectionUtils.rethrowRuntimeException;

public class DefaultCluster<T> extends ZookeeperServerList<T> implements Cluster<T> {
    private final HaStrategy haStrategy;
    private final LoadBalance loadBalance;

    private volatile List<NettyClient> clients = new ArrayList<>();

    public DefaultCluster(Class<T> serviceInterface, ServiceDiscovery<ZookeeperInstance> serviceDiscovery, HaStrategy haStrategy, LoadBalance loadBalance) {
        super(serviceInterface, serviceDiscovery);
        this.haStrategy = haStrategy;
        this.loadBalance = loadBalance;
    }

    @Override
    protected void doStart() throws Exception {
        List<ZookeeperServer> initServers = getInitialListOfServers();
        setClients(resovleClients(initServers));
    }

    @Override
    public LoadBalance getLoadBalance() {
        return this.loadBalance;
    }

    @Override
    public HaStrategy getHaStrategy() {
        return this.haStrategy;
    }

    @Override
    public List<NettyClient> getClients() {
        return Collections.unmodifiableList(clients);
    }

    public void setClients(List<NettyClient> updateClients) {
        ArrayList<NettyClient> oldClients = new ArrayList<NettyClient>();
        ArrayList<NettyClient> allClients = new ArrayList<NettyClient>();

        for (NettyClient client : updateClients) {
            if (client == null) {
                continue;
            }
            try {
                client.start();
                allClients.add(client);
            } catch (Exception e) {
                rethrowRuntimeException(e);
            }
        }

        for (NettyClient client : clients) {
            if (!allClients.contains(client)) {
                oldClients.add(client);
            }
        }

        this.clients = allClients;

        for (NettyClient client : oldClients) {
            try {
                client.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public ResponsePromise send(Request request) throws TransportException {
        try {
            return haStrategy.call(request, loadBalance, getClients());
        } catch (Exception e) {
            throw new TransportException(e);
        }
    }

    @Override
    public void onRefresh() {
        List<ZookeeperServer> updatedServers = getUpdatedListOfServers();
        setClients(resovleClients(updatedServers));
    }

    private List<NettyClient> resovleClients(List<ZookeeperServer> servers) {
        if (servers == null || servers.size() == 0) {
            return Collections.EMPTY_LIST;
        }

        List<NettyClient> result = new ArrayList<>();
        for (ZookeeperServer server : servers) {
            result.add(new NettyClient(server.getAddress()));
        }

        return result;
    }
}
