package org.whiskeysierra.gestureremote.server;

import org.whiskeysierra.gestureremote.server.model.Server;

public final class Delete {

    private final Server server;

    public Delete(Server server) {
        this.server = server;
    }

    public Server getServer() {
        return server;
    }

}
