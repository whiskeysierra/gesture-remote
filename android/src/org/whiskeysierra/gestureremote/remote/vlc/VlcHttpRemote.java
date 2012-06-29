package org.whiskeysierra.gestureremote.remote.vlc;

import android.os.AsyncTask;
import com.google.common.base.Strings;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import org.nnsoft.guice.lifegycle.AfterInjection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.whiskeysierra.gestureremote.command.playback.State;
import org.whiskeysierra.gestureremote.command.playback.StateUpdate;
import org.whiskeysierra.gestureremote.remote.Remote;
import org.whiskeysierra.gestureremote.remote.ServerError;
import org.whiskeysierra.gestureremote.server.Connect;
import org.whiskeysierra.gestureremote.server.Connected;
import org.whiskeysierra.gestureremote.server.Delete;
import org.whiskeysierra.gestureremote.server.Disconnect;
import org.whiskeysierra.gestureremote.server.model.Server;
import org.xml.sax.SAXException;

import javax.annotation.Nullable;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

final class VlcHttpRemote implements Remote {

    private static final Logger LOG = LoggerFactory.getLogger(VlcHttpRemote.class);

    private Server server;

    @Inject
    private EventBus bus;

    private DocumentBuilder builder;

    public VlcHttpRemote() {
        try {
            this.builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new IllegalStateException(e);
        }
    }

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
    public void onConnect(Connect connect) throws ExecutionException, InterruptedException {
        if (server != null) {
            bus.post(new Disconnect(server));
        }
        server = connect.getServer();

        if (executeAndUpdateStatus(null, null).get()) {
            bus.post(new Connected(server));
        }
    }

    final class VlcHttpRemoteTask extends AsyncTask<Object, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Object... params) {
            final String command = (String) params[0];
            final String value = (String) params[1];
            final Boolean parse = (Boolean) params[2];

            try {
                final String uri = "/requests/status.xml";
                final String query;

                if (Strings.isNullOrEmpty(command)) {
                    query = "";
                } else if (Strings.isNullOrEmpty(value)) {
                    query = "?command=" + command;
                } else {
                    query = "?command=" + command + "&val=" + URLEncoder.encode(value);
                }

                LOG.debug("Sending {}{}", uri, query);
                final URL url = new URL("http", server.getHost(), server.getPort(), uri + query);
                final URLConnection connection = url.openConnection();

                final InputStream stream = connection.getInputStream();

                try {
                    if (parse) {
                        final Document document = builder.parse(stream);

                        final Node stateNode = document.getElementsByTagName("state").item(0);
                        final State state = State.valueOf(stateNode.getTextContent().toUpperCase(Locale.ENGLISH));
                        bus.post(new StateUpdate(state));
                    }
                } finally {
                    stream.close();
                }

                return true;
            } catch (MalformedURLException e) {
                LOG.info("Malformed url?!", e);
                bus.post(new ServerError(e.getMessage()));
            } catch (IOException e) {
                LOG.error("Error accessing server " + server, e);
                bus.post(new ServerError(e.getMessage()));
            } catch (SAXException e) {
                LOG.error("Failed to parse xml", e);
                bus.post(new ServerError(e.getMessage()));
            }

            return false;
        }

    }

    private AsyncTask<Object, Void, Boolean> execute(@Nullable String command) {
        return execute(command, null);
    }

    private AsyncTask<Object, Void, Boolean> execute(@Nullable String command, @Nullable String value) {
        return executeAndUpdateStatus(command, value);
    }


    private AsyncTask<Object, Void, Boolean> executeAndUpdateStatus(@Nullable String command, @Nullable String value) {
        if (server == null) {
            LOG.info("Not sending {}, because we are not connected to a server", command);
            return null;
        }

        return new VlcHttpRemoteTask().execute(command, value, true);
    }


    @Override
    public boolean play() {
        // should be pl_play, but that does not work, because
        // status.xml seems to return the old state, instead of the current
        // in that case
        executeAndUpdateStatus("pl_pause", null);
        return true;
    }

    @Override
    public boolean pause() {
        executeAndUpdateStatus("pl_pause", null);
        return true;
    }

    @Override
    public boolean previous() {
        execute("pl_previous");
        return true;
    }

    @Override
    public boolean next() {
        execute("pl_next");
        return true;
    }

    @Override
    public boolean seek(float percentage) {
        execute("seek", String.format("%.2f%%", percentage));
        return true;
    }

    @Override
    public boolean volume(float percentage) {
        execute("volume", String.format("%+.0f", percentage));
        return true;
    }

    @Override
    public boolean fullscreen() {
        execute("fullscreen");
        return true;
    }

    @Override
    public boolean window() {
        execute("fullscreen");
        return true;
    }

}
