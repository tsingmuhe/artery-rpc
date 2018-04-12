package com.sunchp.artery.springsupport.rpc;

import com.sunchp.artery.springsupport.context.NettyApplicationContext;
import org.springframework.context.ApplicationContext;

public class AppServerTest {
    public static void main(String[] args) {
        ApplicationContext context = new NettyApplicationContext(ArteryServerTestConfiguration.class);
    }
}
