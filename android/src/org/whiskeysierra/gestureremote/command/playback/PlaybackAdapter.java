package org.whiskeysierra.gestureremote.command.playback;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import org.nnsoft.guice.lifegycle.AfterInjection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.whiskeysierra.gestureremote.ViewSize;
import org.whiskeysierra.gestureremote.recognition.gestures.DoubleTap;
import org.whiskeysierra.gestureremote.recognition.gestures.HorizontalDrag;
import org.whiskeysierra.gestureremote.recognition.gestures.Pinch;
import org.whiskeysierra.gestureremote.recognition.gestures.SingleTap;
import org.whiskeysierra.gestureremote.recognition.gestures.VerticalDrag;
import org.whiskeysierra.gestureremote.recognition.gestures.Zoom;

final class PlaybackAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(PlaybackAdapter.class);

    @Inject
    private EventBus bus;

    private State state = State.INITIAL;

    private ViewSize size;

    private EventBuffer volume = new EventBuffer();

    @AfterInjection
    public void onPostConstruct() {
        bus.register(this);
    }

    @Subscribe
    public void onStateUpdate(StateUpdate update) {
        this.state = update.getState();
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
            case STOPPED: {
                play();
                break;
            }
        }
    }

    private void play() {
        bus.post(Play.INSTANCE);
    }

    private void pause() {
        bus.post(Pause.INSTANCE);
    }

    @Subscribe
    public void onHorizontalDrag(HorizontalDrag drag) {
        final float value = drag.getX() / size.getWidth() * 100f;
        bus.post(new Seek(value));
    }

    @Subscribe
    public void onVerticalDrag(VerticalDrag drag) {
        final float delta = drag.getDistance() / size.getHeight() * 100f;
        final float value = volume.update(delta);

        if (Float.isNaN(value)) {
            return;
        }

        bus.post(new TurnVolume(value));
    }

    @Subscribe
    public void onPinch(Pinch _) {
        bus.post(Window.INSTANCE);
    }

    @Subscribe
    public void onZoom(Zoom _) {
        bus.post(Fullscreen.INSTANCE);
    }

}
