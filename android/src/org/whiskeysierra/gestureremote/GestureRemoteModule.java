package org.whiskeysierra.gestureremote;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import org.nnsoft.guice.lifegycle.AfterInjectionModule;
import org.whiskeysierra.gestureremote.command.CommandModule;
import org.whiskeysierra.gestureremote.recognition.RecognitionModule;
import org.whiskeysierra.gestureremote.remote.RemoteModule;
import org.whiskeysierra.gestureremote.server.ServerModule;

final class GestureRemoteModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(EventBus.class).in(Singleton.class);

        // required to use @AfterInjection
        install(new AfterInjectionModule());

        install(new CommandModule());
        install(new RecognitionModule());
        install(new RemoteModule());
        install(new ServerModule());
    }

}
