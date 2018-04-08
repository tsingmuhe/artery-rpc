package com.sunchp.artery.rpc;

import com.sunchp.artery.api.HelloService;
import com.sunchp.artery.api.HelloServiceImpl;
import com.sunchp.artery.exporter.DefaultExporter;
import com.sunchp.artery.registry.ServiceDiscoveryCustomizer;
import com.sunchp.artery.registry.ZookeeperDiscoveryProperties;
import com.sunchp.artery.registry.ZookeeperInstance;
import com.sunchp.artery.registry.serviceregistry.ZookeeperRegistration;
import com.sunchp.artery.registry.serviceregistry.ZookeeperServiceRegistry;
import com.sunchp.artery.transport.Server;
import com.sunchp.artery.transport.ServerBuilder;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.details.InstanceSerializer;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import org.junit.Test;

import java.io.IOException;

public class ExporterTest {
    @Test
    public void test() throws Exception {
        CuratorFramework curator = CuratorFrameworkFactory.newClient("127.0.0.1", new ExponentialBackoffRetry(1000, 3));
        curator.start();
        curator.blockUntilConnected();

        ZookeeperDiscoveryProperties properties = new ZookeeperDiscoveryProperties();
        InstanceSerializer<ZookeeperInstance> instanceSerializer = new JsonInstanceSerializer<>(ZookeeperInstance.class);
        ServiceDiscoveryCustomizer customizer = new ServiceDiscoveryCustomizer(curator, properties, instanceSerializer);
        ServiceDiscovery<ZookeeperInstance> serviceDiscovery = customizer.customize(ServiceDiscoveryBuilder.builder(ZookeeperInstance.class));
        serviceDiscovery.start();

        ZookeeperServiceRegistry registry = new ZookeeperServiceRegistry(serviceDiscovery);
        ZookeeperRegistration registration = ZookeeperRegistration.builder().address("127.0.0.1").port(8080).name(HelloService.class.getName()).payload(new ZookeeperInstance()).build();
        registry.register(registration);

        Exporter<HelloService> helloService = new DefaultExporter<>(HelloService.class, new HelloServiceImpl());
        Server server = ServerBuilder.forPort(8080).addService(helloService).build();
        server.start();

        Thread.sleep(Integer.MAX_VALUE);
    }
}