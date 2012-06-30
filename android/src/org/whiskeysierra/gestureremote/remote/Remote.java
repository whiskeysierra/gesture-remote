package org.whiskeysierra.gestureremote.remote;

public interface Remote {

    void play();

    void pause();

    void previous();

    void next();

    void seek(float percentage);

    void volume(float percentage);

    void fullscreen();

    void window();

}
