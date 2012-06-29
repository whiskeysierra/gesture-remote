package org.whiskeysierra.gestureremote.remote;

public interface Remote {

    boolean play();

    boolean pause();

    boolean previous();

    boolean next();

    boolean seek(float percentage);

    boolean volume(float percentage);

    boolean fullscreen();

    boolean window();

}
