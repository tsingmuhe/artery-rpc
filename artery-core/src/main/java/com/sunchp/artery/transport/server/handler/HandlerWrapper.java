package com.sunchp.artery.transport.server.handler;

import com.sunchp.artery.rpc.Request;
import com.sunchp.artery.rpc.Response;

public class HandlerWrapper extends AbstractHandlerContainer {
    protected Handler _handler;

    public Handler getHandler() {
        return _handler;
    }

    public void setHandler(Handler handler) {
        if (isStarted())
            throw new IllegalStateException(STARTED);

        if (handler == this) {
            return;
        }

        if (handler != null) {
            handler.setServer(getServer());
        }

        _handler = handler;
    }

    @Override
    public Handler[] getHandlers() {
        if (_handler == null)
            return new Handler[0];
        return new Handler[]{_handler};
    }

    @Override
    public void handle(Request request, Response response) {
        Handler handler = _handler;
        if (handler != null)
            handler.handle(request, response);
    }

    @Override
    public void destroy() {
        if (!isStopped())
            throw new IllegalStateException("!STOPPED");
        Handler child = getHandler();
        if (child != null) {
            setHandler(null);
            child.destroy();
        }

        super.destroy();
    }
}
