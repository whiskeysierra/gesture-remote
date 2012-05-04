package org.whiskeysierra.listeners;

import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.whiskeysierra.gestures.DoubleTap;
import org.whiskeysierra.gestures.SingleTap;

@Singleton
final class SimpleGestureListener extends SimpleOnGestureListener {

    @Inject
    private EventBus bus;

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

}
