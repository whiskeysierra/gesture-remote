package org.whiskeysierra.gestureremote.server;

import org.whiskeysierra.gestureremote.server.model.Server;

public final class Connected {

    private final Server server;

    public Connected(Server server) {
        this.server = server;
    }

    public Server getServer() {
        return server;
    }

}
