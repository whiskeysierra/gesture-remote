package org.whiskeysierra.gestureremote.remote.vlc;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import org.whiskeysierra.gestureremote.remote.Remote;

public final class VlcModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Remote.class).to(VlcHttpRemote.class).in(Singleton.class);
    }

}
