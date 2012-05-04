package org.whiskeysierra.gestureremote.gesture;

import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import org.nnsoft.guice.lifegycle.AfterInjection;
import roboguice.inject.ContextSingleton;

@ContextSingleton
final class SimpleGestureListener extends SimpleOnGestureListener {

    private static final int MIN_DISTANCE = 120;
    private static final int MAXIMUM_OFFSET = 150;
    private static final int VELOCITY_THRESHOLD = 200;

    private final GestureDetector detector = new GestureDetector(this);

    @Inject
    private EventBus bus;

    @Inject
    private ViewConfiguration config;

    @AfterInjection
    public void onPostConstruct() {
        bus.register(this);
    }

    @Subscribe
    public void onMotionEvent(MotionEvent event) {
        detector.onTouchEvent(event);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        bus.post(SingleTap.INSTANCE);
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        bus.post(DoubleTap.INSTANCE);
        return true;
    }

    @Override
    public boolean onFling(MotionEvent a, MotionEvent b, float velocity, float _) {
        if (Math.abs(a.getY() - b.getY()) > MAXIMUM_OFFSET) {
            return false;
        } else if (isLeftOf(a, b, velocity)) {
            bus.post(LeftFling.INSTANCE);
            return true;
        } else if (isLeftOf(b, a, velocity)) {
            bus.post(RightFling.INSTANCE);
            return true;
        } else {
            return false;
        }
    }

    private boolean isLeftOf(MotionEvent b, MotionEvent a, float velocity) {
        return a.getX() - b.getX() > MIN_DISTANCE && Math.abs(velocity) > VELOCITY_THRESHOLD;
    }

}
