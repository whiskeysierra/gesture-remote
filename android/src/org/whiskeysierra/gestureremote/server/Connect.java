package org.whiskeysierra.gestureremote.server;

import org.whiskeysierra.gestureremote.server.model.Server;

public final class Connect {

    private final Server server;

    public Connect(Server server) {
        this.server = server;
    }

    public Server getServer() {
        return server;
    }

}
