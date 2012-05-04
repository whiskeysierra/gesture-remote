package org.whiskeysierra.gestureremote.gesture;

import android.content.Context;
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
final class GestureListener extends SimpleOnGestureListener {

    private final GestureDetector detector = new GestureDetector(this);

    @Inject
    private EventBus bus;

    @Inject
    private Context context;

    @Inject
    private ViewConfiguration config;

    private int minimumDistance;

    private int minimumVelocity;

    @AfterInjection
    public void onPostConstruct() {
        bus.register(this);

        final ViewConfiguration configuration = ViewConfiguration.get(context);
        this.minimumDistance = configuration.getScaledTouchSlop();
        this.minimumVelocity = configuration.getScaledMinimumFlingVelocity();
    }

    @Subscribe
    public void onMotionEvent(MotionEvent event) {
        detector.onTouchEvent(event);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        bus.post(SingleTap.INSTANCE);
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        bus.post(DoubleTap.INSTANCE);
        return false;
    }

    @Override
    public boolean onFling(MotionEvent a, MotionEvent b, float velocityX, float velocityY) {
        if (a.getX() - b.getX() > minimumDistance && Math.abs(velocityX) > minimumVelocity) {
            bus.post(RightFling.INSTANCE);
            return true;
        } else if (b.getX() - a.getX() > minimumDistance && Math.abs(velocityX) > minimumVelocity) {
            bus.post(LeftFling.INSTANCE);
            return true;
        } else {
            return false;
        }
    }

}
