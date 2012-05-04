package org.whiskeysierra.gestureremote;

import android.app.Application;
import com.google.inject.Module;
import com.google.inject.Stage;
import roboguice.RoboGuice;
import roboguice.config.DefaultRoboModule;

public class GestureRemoteApplication extends Application {

    @Override
    public void onCreate() {
        final DefaultRoboModule module = RoboGuice.newDefaultRoboModule(this);
        final Module[] modules = {
            module,
            new GestureRemoteModule()
        };
        RoboGuice.setBaseApplicationInjector(this, Stage.PRODUCTION, modules);
        super.onCreate();
    }

}
