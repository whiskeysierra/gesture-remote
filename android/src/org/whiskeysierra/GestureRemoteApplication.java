package org.whiskeysierra;

import android.app.Application;
import com.google.inject.Module;
import com.google.inject.Stage;
import org.nnsoft.guice.lifegycle.AfterInjectionModule;
import roboguice.RoboGuice;
import roboguice.config.DefaultRoboModule;

public class GestureRemoteApplication extends Application {

    @Override
    public void onCreate() {
        final DefaultRoboModule module = RoboGuice.newDefaultRoboModule(this);
        final Module[] modules = {
            module,
            new AfterInjectionModule(),
            new GestureRemoteModule()
        };
        RoboGuice.setBaseApplicationInjector(this, Stage.PRODUCTION, modules);
        super.onCreate();
    }

}
