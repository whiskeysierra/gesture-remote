package org.whiskeysierra.listeners;

import android.view.GestureDetector;
import android.view.MotionEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import org.nnsoft.guice.lifegycle.AfterInjection;

final class MotionEventListener {

    @Inject
    private EventBus bus;

    @Inject
    private GestureDetector detector;

    @AfterInjection
    public void onPostConstruct() {
        bus.register(this);
    }

    @Subscribe
    public void onMotionEvent(MotionEvent event) {
        detector.onTouchEvent(event);
    }

}
