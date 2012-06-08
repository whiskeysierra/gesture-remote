package org.whiskeysierra.gestureremote.remote.vlc;

import org.whiskeysierra.gestureremote.remote.Remote;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

final class VlcHttpRemote implements Remote {

    private void send(String command) {
        try {
            final URL url = new URL("http", "192.168.100.22", 8080, "/requests/status.xml?command=" + command);
            final URLConnection connection = url.openConnection();
            connection.getContent();
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
