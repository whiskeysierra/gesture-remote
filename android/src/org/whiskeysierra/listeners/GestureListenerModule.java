package org.whiskeysierra.listeners;

import android.view.GestureDetector;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public final class GestureListenerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(MotionEventListener.class).asEagerSingleton();
    }

    @Provides
    public GestureDetector provideGestureDetector(SimpleGestureListener listener) {
        return new GestureDetector(listener);
    }

}
