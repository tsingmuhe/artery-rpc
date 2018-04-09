package com.sunchp.artery.springsupport;

import org.springframework.context.ApplicationContext;

public class AppTest {

    public static void main(String[] args) {
        ApplicationContext context = new NettyApplicationContext(ArteryTestConfiguration.class);

        ArteryServiceConfig arteryServiceConfig = context.getBean(ArteryServiceConfig.class);
        System.out.println(arteryServiceConfig.getExporters().size());
    }
}
