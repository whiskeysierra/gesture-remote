package org.whiskeysierra.gestureremote.remote.vlc;

import android.net.http.AndroidHttpClient;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.params.BasicHttpParams;
import org.whiskeysierra.gestureremote.remote.Remote;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

final class VlcHttpRemote implements Remote {

    private HttpClient client = AndroidHttpClient.newInstance("Android Gesture Remote");
    private String server;

    private void send(String command) {
        if (server == null) return;

        try {
            // TODO cache
            final HttpHost host = new HttpHost("192.168.100.22", 8080, "http");
            final HttpRequest request = new HttpHead("/requests/status-xml");

            request.getParams().setParameter("command", command);

            client.execute(host, request);
        } catch (MalformedURLException e) {
            // TODO don't swallow
        } catch (IOException e) {
            // TODO don't swallow
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
    public void fullscreen() {
        send("fullscreen");
    }

    @Override
    public void window() {
        send("fullscreen");
    }

}
