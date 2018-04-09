package com.sunchp.artery.springsupport;

import com.sunchp.artery.transport.server.Server;
import org.springframework.context.ApplicationEvent;

public class NettyServerInitializedEvent extends ApplicationEvent {

    public NettyServerInitializedEvent(Server server) {
        super(server);
    }
}
