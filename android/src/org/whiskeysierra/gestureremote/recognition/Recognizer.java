package org.whiskeysierra.gestureremote.recognition;

import android.view.MotionEvent;

public interface Recognizer {

    boolean recognize(MotionEvent event);

}
