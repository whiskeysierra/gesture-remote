package org.whiskeysierra.gestureremote.gesture;

import com.google.inject.AbstractModule;

public final class GestureModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(GestureListener.class).asEagerSingleton();
    }

}
