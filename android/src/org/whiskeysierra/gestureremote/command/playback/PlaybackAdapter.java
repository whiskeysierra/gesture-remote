package org.whiskeysierra.gestureremote.command.playback;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import org.nnsoft.guice.lifegycle.AfterInjection;
import org.whiskeysierra.gestureremote.ViewSize;
import org.whiskeysierra.gestureremote.recognition.gestures.DoubleTap;
import org.whiskeysierra.gestureremote.recognition.gestures.Pinch;
import org.whiskeysierra.gestureremote.recognition.gestures.HorizontalDrag;
import org.whiskeysierra.gestureremote.recognition.gestures.SingleTap;
import org.whiskeysierra.gestureremote.recognition.gestures.VerticalDrag;
import org.whiskeysierra.gestureremote.recognition.gestures.Zoom;

final class PlaybackAdapter {

    @Inject
    private EventBus bus;

    private ViewSize size;

    private State state = State.INITIAL;

    @AfterInjection
    public void onPostConstruct() {
        bus.register(this);
    }

    @Subscribe
    public void onResize(ViewSize size) {
        this.size = size;
    }

    @Subscribe
    public void onSingleTap(SingleTap _) {
        onTap();
    }

    @Subscribe
    public void onDoubleTap(DoubleTap _) {
        onTap();
    }

    private void onTap() {
        switch (state) {
            case INITIAL: {
                play();
                break;
            }
            case PLAYING: {
                pause();
                break;
            }
            case PAUSED: {
                play();
                break;
            }
        }

        state = state.next();
    }

    private void play() {
        bus.post(Play.INSTANCE);
    }

    private void pause() {
        bus.post(Pause.INSTANCE);
    }

    @Subscribe
    public void onPinch(Pinch _) {
        bus.post(Window.INSTANCE);
    }

    @Subscribe
    public void onZoom(Zoom _) {
        bus.post(Fullscreen.INSTANCE);
    }

    @Subscribe
    public void onHorizontalDrag(HorizontalDrag drag) {
        bus.post(new Seek(drag.getDistance() / size.getWidth() * 100));
    }

    @Subscribe
    public void onVerticalDrag(VerticalDrag drag) {
        bus.post(new TurnVolume(drag.getDistance() / size.getHeight() * 100));
    }

}
