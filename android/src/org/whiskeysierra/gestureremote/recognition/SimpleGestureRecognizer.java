package org.whiskeysierra.gestureremote.recognition;

import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import org.nnsoft.guice.lifegycle.AfterInjection;
import org.whiskeysierra.gestureremote.util.ViewSize;
import org.whiskeysierra.gestureremote.recognition.gestures.DoubleTap;
import org.whiskeysierra.gestureremote.recognition.gestures.LeftFling;
import org.whiskeysierra.gestureremote.recognition.gestures.HorizontalDrag;
import org.whiskeysierra.gestureremote.recognition.gestures.RightFling;
import org.whiskeysierra.gestureremote.recognition.gestures.SingleTap;
import org.whiskeysierra.gestureremote.recognition.gestures.VerticalDrag;

final class SimpleGestureRecognizer extends SimpleOnGestureListener implements Recognizer {

    private static final int MIN_DISTANCE = 120;
    private static final int MAXIMUM_OFFSET = 150;
    private static final int VELOCITY_THRESHOLD = 200;

    private final GestureDetector detector = new GestureDetector(this);

    private MotionEvent scroll;

    @Inject
    private EventBus bus;

    @Inject
    private ViewConfiguration config;

    private ViewSize size;

    @AfterInjection
    public void onPostConstruct() {
        bus.register(this);
    }

    @Subscribe
    public void onResize(ViewSize size) {
        this.size = size;
    }

    @Override
    public boolean recognize(MotionEvent event) {
        if (detector.onTouchEvent(event)) {
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_UP && scroll != null) {
            bus.post(new HorizontalDrag(scroll.getX()));
            scroll = null;
        }
        return false;
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

    @Override
    public boolean onScroll(MotionEvent first, MotionEvent second, float distanceX, float distanceY) {
        final float minY = 0.8f * size.getHeight();
        final float minX = 0.8f * size.getWidth();

        if (first.getY() > minY && second.getY() > minY) {
            this.scroll = second;
            return true;
        } else if (first.getX() > minX && second.getX() > minX) {
            bus.post(new VerticalDrag(+distanceY));
            return true;
        }

        return false;
    }

    private boolean isLeftOf(MotionEvent b, MotionEvent a, float velocity) {
        return a.getX() - b.getX() > MIN_DISTANCE && Math.abs(velocity) > VELOCITY_THRESHOLD;
    }

}
