package org.whiskeysierra.gestureremote.remote.vlc;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import org.nnsoft.guice.lifegycle.AfterInjection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.whiskeysierra.gestureremote.remote.Remote;
import org.whiskeysierra.gestureremote.server.Connect;
import org.whiskeysierra.gestureremote.server.Connected;
import org.whiskeysierra.gestureremote.server.Delete;
import org.whiskeysierra.gestureremote.server.Disconnect;
import org.whiskeysierra.gestureremote.server.model.Server;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

final class VlcHttpRemote implements Remote {

    private static final Logger LOG = LoggerFactory.getLogger(VlcHttpRemote.class);

    private Server server;

    @Inject
    private EventBus bus;

    @AfterInjection
    public void onPostConstruct() {
        bus.register(this);
    }

    @Subscribe
    public void onDelete(Delete delete) {
        if (delete.getServer().equals(server)) {
            bus.post(new Disconnect(server));
            server = null;
        }
    }

    @Subscribe
    public void onConnect(Connect connect) {
        if (server != null) {
            bus.post(new Disconnect(server));
        }
        server = connect.getServer();

        // TODO receive status and only post connected event, if successful


        bus.post(new Connected(server));
    }

    private void send(String command) {
        if (server == null) {
            LOG.info("Not sending {}, because we are not connected to a server", command);
            return;
        }

        try {
            final String uri = "/requests/status.xml?command=" + command;
            final URL url = new URL("http", server.getHost(), server.getPort(), uri);
            final URLConnection connection = url.openConnection();
            final Object content = connection.getContent();

            // TODO parse xml content and save as status info
        } catch (MalformedURLException e) {
            throw new IllegalStateException(e);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private void send(String command, String value) {

        final String query = "command=" + command + "&val=" + URLEncoder.encode(value);

        try {
            final URL url = new URL("http", "192.168.100.22", 8080, "/requests/status.xml?" + query);
            final URLConnection connection = url.openConnection();
            connection.getContent();
        } catch (MalformedURLException e) {
            throw new IllegalStateException(e);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void play() {
        send("pl_play");
    }

    @Override
    public void pause() {
        send("pl_pause");
    }

    @Override
    public void previous() {
        send("pl_previous");
    }

    @Override
    public void next() {
        send("pl_next");
    }

    @Override
    public void seek(float percentage) {
        send("seek", String.format("%.2f", percentage));
    }

    @Override
    public void volume(float percentage) {
        final String format = String.format("%.2f", percentage);
        send("volume", percentage > 0 ? "+" + format : format);
    }

    @Override
    public void fullscreen() {
        send("fullscreen");
    }

    @Override
    public void window() {
        send("fullscreen");
    }

}
