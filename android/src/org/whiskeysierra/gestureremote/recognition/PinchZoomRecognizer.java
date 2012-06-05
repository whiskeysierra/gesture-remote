package org.whiskeysierra.gestureremote.recognition;

import android.content.Context;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import org.nnsoft.guice.lifegycle.AfterInjection;
import org.whiskeysierra.gestureremote.recognition.gestures.Pinch;
import org.whiskeysierra.gestureremote.recognition.gestures.Zoom;

final class PinchZoomRecognizer extends SimpleOnScaleGestureListener implements Recognizer {

    @Inject
    private Context context;

    @Inject
    private EventBus bus;

    private ScaleGestureDetector detector;

    @AfterInjection
    public void onPostConstruct() {
        detector = new ScaleGestureDetector(context, this);
    }

    @Override
    public boolean recognize(MotionEvent event) {
        return detector.onTouchEvent(event);
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
