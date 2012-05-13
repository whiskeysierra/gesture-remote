package org.whiskeysierra.gestureremote.servermanagment.data;

/**
 * Created with IntelliJ IDEA.
 * User: afeldmann
 * Date: 11.05.12
 * Time: 17:27
 * To change this template use File | Settings | File Templates.
 */
public class Settings {

    private long id;
    private String host;
    private int port;
    private boolean selected;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return name+" "+host+":"+port;
    }
}
