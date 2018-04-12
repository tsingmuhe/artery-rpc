package com.sunchp.artery.springsupport.transport;

import com.sunchp.artery.transport.client.Client;
import com.sunchp.artery.transport.client.netty.NettyClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;

@Configuration
public class AppClientTestConfiguration {
    @Bean
    public Client client() throws Exception {
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8080);
        Client client = new NettyClient(address);
        client.start();

        return client;
    }
}
