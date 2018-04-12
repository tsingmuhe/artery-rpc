package com.sunchp.artery.springsupport.rpc;

import com.sunchp.artery.registry.ServiceDiscoveryCustomizer;
import com.sunchp.artery.registry.ZookeeperDiscoveryProperties;
import com.sunchp.artery.registry.ZookeeperInstance;
import com.sunchp.artery.springsupport.ArteryRefererFactoryBean;
import com.sunchp.artery.springsupport.api.HelloService;
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

@Configuration
@ComponentScan("com.sunchp.artery.springsupport.api")
public class AppClientTestConfiguration {
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

    @Bean
    public ArteryRefererFactoryBean<HelloService> helloService(ServiceDiscovery<ZookeeperInstance> serviceDiscovery) {
        ArteryRefererFactoryBean<HelloService> bean = new ArteryRefererFactoryBean<>(HelloService.class, serviceDiscovery);
        return bean;
    }
}
