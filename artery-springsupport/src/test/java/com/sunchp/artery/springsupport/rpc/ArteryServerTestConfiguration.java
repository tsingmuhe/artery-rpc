package com.sunchp.artery.springsupport.rpc;

import com.sunchp.artery.exporter.DefaultExporterContainer;
import com.sunchp.artery.registry.ServiceDiscoveryCustomizer;
import com.sunchp.artery.registry.ZookeeperDiscoveryProperties;
import com.sunchp.artery.registry.ZookeeperInstance;
import com.sunchp.artery.registry.serviceregistry.ZookeeperServiceRegistry;
import com.sunchp.artery.rpc.ExporterContainer;
import com.sunchp.artery.springsupport.ArteryExporterConfig;
import com.sunchp.artery.springsupport.context.NettyServerFactory;
import com.sunchp.artery.transport.server.handler.ExporterContainerHandler;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.details.InstanceSerializer;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;

@Configuration
@ComponentScan("com.sunchp.artery.springsupport.impl")
public class ArteryServerTestConfiguration {
    private static final String DEFAULT_HOST = "127.0.0.1";
    private static final int DEFAULT_PORT = 8080;

    private String host = DEFAULT_HOST;
    private int port = DEFAULT_PORT;

    @Bean
    public NettyServerFactory nettyServerFactory(ExporterContainer exporterContainer) {
        InetSocketAddress address = new InetSocketAddress(host, port);
        NettyServerFactory nettyServerFactory = new NettyServerFactory();
        nettyServerFactory.setAddress(address);
        nettyServerFactory.setHandler(new ExporterContainerHandler(exporterContainer));
        return nettyServerFactory;
    }

    @Bean
    public ExporterContainer exporterContainer() {
        return new DefaultExporterContainer();
    }

    @Bean
    public ArteryExporterConfig arteryExporterConfig(ExporterContainer exporterContainer, ZookeeperServiceRegistry zookeeperServiceRegistry) {
        ArteryExporterConfig bean = new ArteryExporterConfig();
        bean.setHost(this.host);
        bean.setPort(this.port);
        bean.setExporterContainer(exporterContainer);
        bean.setZookeeperServiceRegistry(zookeeperServiceRegistry);
        return bean;
    }

    @Bean
    public ZookeeperServiceRegistry zookeeperServiceRegistry(ServiceDiscovery<ZookeeperInstance> serviceDiscovery) {
        ZookeeperServiceRegistry registry = new ZookeeperServiceRegistry(serviceDiscovery);
        return registry;
    }

    @Bean
    public ServiceDiscovery<ZookeeperInstance> serviceDiscovery() throws Exception {
        CuratorFramework curator = CuratorFrameworkFactory.newClient("127.0.0.1", new ExponentialBackoffRetry(1000, 3));
        curator.start();
        curator.blockUntilConnected();

        ZookeeperDiscoveryProperties properties = new ZookeeperDiscoveryProperties();
        InstanceSerializer<ZookeeperInstance> instanceSerializer = new JsonInstanceSerializer<>(ZookeeperInstance.class);
        ServiceDiscoveryCustomizer customizer = new ServiceDiscoveryCustomizer(curator, properties, instanceSerializer);
        ServiceDiscovery<ZookeeperInstance> serviceDiscovery = customizer.customize(ServiceDiscoveryBuilder.builder(ZookeeperInstance.class));
        serviceDiscovery.start();
        return serviceDiscovery;
    }
}
