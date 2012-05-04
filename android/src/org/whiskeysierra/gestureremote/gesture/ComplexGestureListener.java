package org.whiskeysierra.gestureremote.gesture;

import android.content.Context;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import org.nnsoft.guice.lifegycle.AfterInjection;
import roboguice.inject.ContextSingleton;

@ContextSingleton
final class ComplexGestureListener extends SimpleOnScaleGestureListener {

    @Inject
    private Context context;

    @Inject
    private EventBus bus;

    private ScaleGestureDetector detector;

    @AfterInjection
    public void onPostConstruct() {
        bus.register(this);
        detector = new ScaleGestureDetector(context, this);
    }

    @Subscribe
    public void onMotionEvent(MotionEvent event) {
        detector.onTouchEvent(event);
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        if (detector.getScaleFactor() < 1f) {
            bus.post(Pinch.INSTANCE);
        } else {
            bus.post(Zoom.INSTANCE);
        }
    }

}
