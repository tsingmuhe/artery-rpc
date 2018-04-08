package com.sunchp.artery.rpc;

import com.sunchp.artery.api.HelloService;
import com.sunchp.artery.api.Person;
import com.sunchp.artery.proxy.jdk.JdkProxyFactory;
import com.sunchp.artery.referer.DefaultReferer;
import com.sunchp.artery.registry.ServiceDiscoveryCustomizer;
import com.sunchp.artery.registry.ZookeeperDiscoveryProperties;
import com.sunchp.artery.registry.ZookeeperInstance;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.details.InstanceSerializer;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import org.junit.Test;

public class RefererTest {
    @Test
    public void test1() throws Exception {
        CuratorFramework curator = CuratorFrameworkFactory.newClient("127.0.0.1", new ExponentialBackoffRetry(1000, 3));
        curator.start();
        curator.blockUntilConnected();

        ZookeeperDiscoveryProperties properties = new ZookeeperDiscoveryProperties();
        InstanceSerializer<ZookeeperInstance> instanceSerializer = new JsonInstanceSerializer<>(ZookeeperInstance.class);
        ServiceDiscoveryCustomizer customizer = new ServiceDiscoveryCustomizer(curator, properties, instanceSerializer);
        ServiceDiscovery<ZookeeperInstance> serviceDiscovery = customizer.customize(ServiceDiscoveryBuilder.builder(ZookeeperInstance.class));
        serviceDiscovery.start();


        Referer<HelloService> referer = new DefaultReferer<>(HelloService.class, new JdkProxyFactory(), serviceDiscovery);
        HelloService helloService = referer.getProxy();
        String result = helloService.hello("sunchp");
        System.out.println(result);

        Person person = helloService.hello("sun", "changpeng");
        System.out.println(person);

    }
}