package org.whiskeysierra.gestureremote.command.playback;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import org.nnsoft.guice.lifegycle.AfterInjection;
import org.whiskeysierra.gestureremote.gesture.DoubleTap;
import org.whiskeysierra.gestureremote.gesture.SingleTap;

final class PlaybackAdapter {

    @Inject
    private EventBus bus;

    private State state = State.INITIAL;

    @AfterInjection
    public void onPostConstruct() {
        bus.register(this);
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

}
