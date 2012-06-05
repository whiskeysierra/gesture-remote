package org.whiskeysierra.gestureremote.recognition;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import java.util.Arrays;
import java.util.List;

public final class GestureModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(RecognitionService.class).asEagerSingleton();
    }

    @Provides
    @Singleton
    public List<Recognizer> provideRecognizers(SimpleGestureRecognizer simpleGestureRecognizer,
        PinchZoomRecognizer pinchZoomRecognizer) {
        return Arrays.asList(simpleGestureRecognizer, pinchZoomRecognizer);
    }

}
