package org.whiskeysierra.gestureremote.command.playback;

import com.google.inject.AbstractModule;

public final class PlaybackModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(PlaybackAdapter.class).asEagerSingleton();
    }

}
