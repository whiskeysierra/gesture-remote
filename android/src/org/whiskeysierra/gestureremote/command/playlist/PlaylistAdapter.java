package org.whiskeysierra.gestureremote.command.playlist;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import org.nnsoft.guice.lifegycle.AfterInjection;
import org.whiskeysierra.gestureremote.recognition.gestures.LeftFling;
import org.whiskeysierra.gestureremote.recognition.gestures.RightFling;

final class PlaylistAdapter {

    @Inject
    private EventBus bus;

    @AfterInjection
    public void onPostConstruct() {
        bus.register(this);
    }

    @Subscribe
    public void onLeftFling(LeftFling _) {
        bus.post(Previous.INSTANCE);
    }

    @Subscribe
    public void onRightFling(RightFling _) {
        bus.post(Next.INSTANCE);
    }

}
