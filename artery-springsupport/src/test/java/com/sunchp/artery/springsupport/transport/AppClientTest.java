package com.sunchp.artery.springsupport.transport;

import com.sunchp.artery.rpc.Request;
import com.sunchp.artery.rpc.ResponsePromise;
import com.sunchp.artery.transport.client.Client;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.UUID;

public class AppClientTest {
    public static void main(String[] args) throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppClientTestConfiguration.class);
        Client client = context.getBean(Client.class);

        Request request = new Request();
        request.setRequestId(UUID.randomUUID().toString());
        ResponsePromise responsePromise = client.send(request);
        System.out.println(responsePromise.get());

        System.out.println("sunch");
    }
}
