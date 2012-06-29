package org.whiskeysierra.gestureremote.server;

import org.whiskeysierra.gestureremote.server.model.Server;

public final class Disconnect {

    private final Server server;

    public Disconnect(Server server) {
        this.server = server;
    }

    public Server getServer() {
        return server;
    }

}
