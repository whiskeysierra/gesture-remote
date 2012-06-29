package org.whiskeysierra.gestureremote.server;

import com.google.inject.AbstractModule;
import roboguice.inject.ContextSingleton;

public final class ServerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(SQLiteHelper.class).in(ContextSingleton.class);
        bind(ServerManager.class).in(ContextSingleton.class);
    }

}
