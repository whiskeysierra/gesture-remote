package org.whiskeysierra.gestureremote.recognition;

import android.graphics.PointF;
import android.view.MotionEvent;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

final class ContinuousCircleRecognizer implements Recognizer {

    /**
     * The center of the circle gesture recognition area.
     * This is usually the center of the screen.
     */
    private PointF center;

    /**
     * The inner radius of the circle gesture recognition area.
     */
    private float innerRadius;

    /**
     * The outer radius of the circle gesture recognition area.
     */
    private float outerRadius;

    /**
     * Timestamp in nanoseconds when the gesture started.
     */
    private long start;

    private float angle;

    private MotionEvent last;

    @Inject
    private EventBus bus;

    @Override
    public boolean recognize(MotionEvent event) {
        last = event;
        return false;
    }

}
