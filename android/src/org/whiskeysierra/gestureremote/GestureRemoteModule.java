package org.whiskeysierra.gestureremote;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import org.nnsoft.guice.lifegycle.AfterInjectionModule;
import org.whiskeysierra.gestureremote.command.CommandModule;
import org.whiskeysierra.gestureremote.gesture.GestureModule;
import org.whiskeysierra.gestureremote.remote.RemoteModule;

final class GestureRemoteModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(EventBus.class).in(Singleton.class);

        install(new AfterInjectionModule());
        install(new CommandModule());
        install(new GestureModule());
        install(new RemoteModule());
    }

}
