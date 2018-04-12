package com.sunchp.artery.springsupport.transport;

import com.sunchp.artery.springsupport.context.NettyServerFactory;
import com.sunchp.artery.transport.server.handler.ThreadPoolExporterContainerHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;

@Configuration
@ComponentScan("com.sunchp.artery.springsupport.api")
public class ArteryServerTestConfiguration {
    private static final String DEFAULT_HOST = "127.0.0.1";
    private static final int DEFAULT_PORT = 8080;

    private String host = DEFAULT_HOST;
    private int port = DEFAULT_PORT;

    @Bean
    public NettyServerFactory nettyServerFactory() {
        InetSocketAddress address = new InetSocketAddress(host, port);
        NettyServerFactory nettyServerFactory = new NettyServerFactory();
        nettyServerFactory.setAddress(address);
        nettyServerFactory.setHandler(new TestHandler());
        return nettyServerFactory;
    }
}
