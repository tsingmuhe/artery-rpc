package com.sunchp.artery.transport.server.handler;

import com.sunchp.artery.rpc.Request;
import com.sunchp.artery.rpc.Response;
import com.sunchp.artery.transport.server.Server;
import com.sunchp.artery.utils.component.Destroyable;
import com.sunchp.artery.utils.component.LifeCycle;

public interface Handler extends LifeCycle, Destroyable {
    public void handle(Request request, Response response);

    public void setServer(Server server);

    public Server getServer();
}
