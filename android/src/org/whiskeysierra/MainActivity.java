package org.whiskeysierra;

import android.gesture.Gesture;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import org.nnsoft.guice.lifegycle.AfterInjection;
import org.whiskeysierra.R.id;
import org.whiskeysierra.gestures.DoubleTap;
import org.whiskeysierra.gestures.SingleTap;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class MainActivity extends RoboActivity implements OnGesturePerformedListener  {

    @Inject
    private EventBus bus;

    @InjectView(id.text)
    private TextView text;

    @AfterInjection
    public void onPostConstruct() {
        bus.register(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        bus.post(event);
        return super.dispatchTouchEvent(event);
    }

    @Subscribe
    public void onSingleTap(SingleTap _) {
        text.setText("Single tap!");
    }

    @Subscribe
    public void onDoubleTap(DoubleTap _) {
        text.setText("Double tap!");
    }

    @Override
    public void onGesturePerformed(GestureOverlayView gestureOverlayView, Gesture gesture) {
        //Log.d(getClass().getName(), "Gesture performed: " + gesture);
    }

}
