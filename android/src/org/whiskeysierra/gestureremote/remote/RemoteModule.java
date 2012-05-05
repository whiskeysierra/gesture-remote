package org.whiskeysierra.gestureremote.remote;

import com.google.inject.AbstractModule;
import org.whiskeysierra.gestureremote.remote.vlc.VlcModule;

public final class RemoteModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(RemoteControlAdapter.class).asEagerSingleton();

        install(new VlcModule());
    }

}
