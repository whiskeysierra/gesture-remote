package org.whiskeysierra.gestureremote.recognition;

import android.view.MotionEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import org.nnsoft.guice.lifegycle.AfterInjection;

import java.util.List;

final class RecognitionService {

    private final List<Recognizer> recognizers;
    private final EventBus bus;

    @Inject
    public RecognitionService(List<Recognizer> recognizers, EventBus bus) {
        this.recognizers = recognizers;
        this.bus = bus;
    }

    @AfterInjection
    public void onPostConstruct() {
        bus.register(this);
    }

    @Subscribe
    public void onMotionEvent(MotionEvent event) {
        for (Recognizer recognizer : recognizers) {
            if (recognizer.recognize(event)) {
                return;
            }
        }
    }


}
