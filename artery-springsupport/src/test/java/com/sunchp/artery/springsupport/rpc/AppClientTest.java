package com.sunchp.artery.springsupport.rpc;

import com.sunchp.artery.springsupport.api.HelloService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AppClientTest {
    public static void main(String[] args) throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppClientTestConfiguration.class);
        HelloService helloService = context.getBean(HelloService.class);

        String result = helloService.hello("sunchp");
        System.out.println(result);
    }
}
