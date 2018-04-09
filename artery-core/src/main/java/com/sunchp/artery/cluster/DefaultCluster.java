package com.sunchp.artery.cluster;

import com.sunchp.artery.registry.ZookeeperInstance;
import com.sunchp.artery.registry.discovery.ZookeeperServer;
import com.sunchp.artery.registry.discovery.ZookeeperServerList;
import com.sunchp.artery.rpc.Request;
import com.sunchp.artery.rpc.ResponsePromise;
import com.sunchp.artery.transport.TransportException;
import com.sunchp.artery.transport.client.Client;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.sunchp.artery.utils.ReflectionUtils.rethrowRuntimeException;

public class DefaultCluster<T> extends ZookeeperServerList<T> implements Cluster<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultCluster.class);

    private final HaStrategy haStrategy;
    private final LoadBalance loadBalance;

    private volatile List<Client> clients = new ArrayList<>();

    public DefaultCluster(Class<T> serviceInterface, ServiceDiscovery<ZookeeperInstance> serviceDiscovery, HaStrategy haStrategy, LoadBalance loadBalance) {
        super(serviceInterface, serviceDiscovery);
        this.haStrategy = haStrategy;
        this.loadBalance = loadBalance;
    }

    @Override
    public void init() {
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
    public List<Client> getClients() {
        return Collections.unmodifiableList(clients);
    }

    public void setClients(List<Client> updateClients) {
        ArrayList<Client> oldClients = new ArrayList<Client>();
        ArrayList<Client> allClients = new ArrayList<Client>();

        for (Client client : updateClients) {
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

        for (Client client : clients) {
            if (!allClients.contains(client)) {
                oldClients.add(client);
            }
        }

        this.clients = allClients;

        for (Client client : oldClients) {
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

    private List<Client> resovleClients(List<ZookeeperServer> servers) {
        if (servers == null || servers.size() == 0) {
            return Collections.EMPTY_LIST;
        }

        List<Client> result = new ArrayList<>();
        for (ZookeeperServer server : servers) {
            result.add(new Client(server.getAddress().getHostString(), server.getAddress().getPort()));
        }

        return result;
    }
}
