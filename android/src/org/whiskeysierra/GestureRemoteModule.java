package org.whiskeysierra;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import org.whiskeysierra.listeners.GestureListenerModule;

final class GestureRemoteModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(EventBus.class).in(Singleton.class);

        install(new GestureListenerModule());
    }

}
