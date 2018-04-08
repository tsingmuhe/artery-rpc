package com.sunchp.artery.registry;

import com.sunchp.artery.api.HelloService;
import com.sunchp.artery.api.HelloServiceImpl;
import com.sunchp.artery.registry.discovery.ServerListWatcherListener;
import com.sunchp.artery.registry.discovery.ZookeeperServerList;
import com.sunchp.artery.registry.serviceregistry.ZookeeperRegistration;
import com.sunchp.artery.registry.serviceregistry.ZookeeperServiceRegistry;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.InstanceSerializer;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import org.junit.Test;

public class RegistryTest {

    @Test
    public void test() throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1", new ExponentialBackoffRetry(1000, 3));
        client.start();
        client.blockUntilConnected();

        ServiceDiscovery<HelloService> serviceDiscovery = ServiceDiscoveryBuilder.builder(HelloService.class)
                .client(client)
                .serializer(new JsonInstanceSerializer<>(HelloService.class))
                .basePath("app")
                .build();

        ServiceInstance<HelloService> instance = ServiceInstance.<HelloService>builder()
                .address("127.0.0.1")
                .port(8080)
                .name("com.sunchp.artery.api.HelloService")
                .payload(new HelloServiceImpl())
                .build();

        serviceDiscovery.registerService(instance);
        serviceDiscovery.start();

        Thread.sleep(Integer.MAX_VALUE);
    }

    @Test
    public void registry() throws Exception {
        CuratorFramework curator = CuratorFrameworkFactory.newClient("127.0.0.1", new ExponentialBackoffRetry(1000, 3));
        curator.start();
        curator.blockUntilConnected();

        ZookeeperDiscoveryProperties properties = new ZookeeperDiscoveryProperties();
        InstanceSerializer<ZookeeperInstance> instanceSerializer = new JsonInstanceSerializer<>(ZookeeperInstance.class);
        ServiceDiscoveryCustomizer customizer = new ServiceDiscoveryCustomizer(curator, properties, instanceSerializer);
        ServiceDiscovery<ZookeeperInstance> serviceDiscovery = customizer.customize(ServiceDiscoveryBuilder.builder(ZookeeperInstance.class));
        serviceDiscovery.start();

        ZookeeperServiceRegistry registry = new ZookeeperServiceRegistry(serviceDiscovery);
        ZookeeperRegistration registration = ZookeeperRegistration.builder().address("127.0.0.1").port(8080).name("com.sunchp.artery.api.HelloService").payload(new ZookeeperInstance()).build();
        registry.register(registration);

        Thread.sleep(Integer.MAX_VALUE);
    }

    @Test
    public void discovery() throws Exception {
        CuratorFramework curator = CuratorFrameworkFactory.newClient("127.0.0.1", new ExponentialBackoffRetry(1000, 3));
        curator.start();
        curator.blockUntilConnected();

        ZookeeperDiscoveryProperties properties = new ZookeeperDiscoveryProperties();
        InstanceSerializer<ZookeeperInstance> instanceSerializer = new JsonInstanceSerializer<>(ZookeeperInstance.class);
        ServiceDiscoveryCustomizer customizer = new ServiceDiscoveryCustomizer(curator, properties, instanceSerializer);
        ServiceDiscovery<ZookeeperInstance> serviceDiscovery = customizer.customize(ServiceDiscoveryBuilder.builder(ZookeeperInstance.class));
        serviceDiscovery.start();

        ZookeeperServerList zookeeperServerList=new ZookeeperServerList(HelloService.class,serviceDiscovery);
        zookeeperServerList.watch();

        Thread.sleep(Integer.MAX_VALUE);
    }
}