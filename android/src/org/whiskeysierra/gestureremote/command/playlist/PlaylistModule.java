package org.whiskeysierra.gestureremote.command.playlist;

import com.google.inject.AbstractModule;

public final class PlaylistModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(PlaylistAdapter.class).asEagerSingleton();
    }

}
