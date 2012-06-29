package org.whiskeysierra.gestureremote.remote;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import org.nnsoft.guice.lifegycle.AfterInjection;
import org.whiskeysierra.gestureremote.command.playback.Fullscreen;
import org.whiskeysierra.gestureremote.command.playback.Pause;
import org.whiskeysierra.gestureremote.command.playback.Paused;
import org.whiskeysierra.gestureremote.command.playback.Play;
import org.whiskeysierra.gestureremote.command.playback.Playing;
import org.whiskeysierra.gestureremote.command.playback.Seek;
import org.whiskeysierra.gestureremote.command.playback.TurnVolume;
import org.whiskeysierra.gestureremote.command.playback.Window;
import org.whiskeysierra.gestureremote.command.playlist.Next;
import org.whiskeysierra.gestureremote.command.playlist.Previous;

final class RemoteControlAdapter {

    @Inject
    private EventBus bus;

    @Inject
    private Remote remote;

    @AfterInjection
    public void onPostConstruct() {
        bus.register(this);
    }

    @Subscribe
    public void onPlay(Play _) {
        if (remote.play()) {
            bus.post(Playing.INSTANCE);
        }
    }

    @Subscribe
    public void onPause(Pause _) {
        if (remote.pause()) {
            bus.post(Paused.INSTANCE);
        }
    }

    @Subscribe
    public void onNext(Next _) {
        remote.next();
    }

    @Subscribe
    public void onPrevious(Previous _) {
        remote.previous();
    }

    @Subscribe
    public void onSeek(Seek seek) {
        remote.seek(seek.getPercentage());
    }

    @Subscribe
    public void onTurnVolume(TurnVolume volume) {
        remote.volume(volume.getPercentage());
    }

    @Subscribe
    public void onFullscreen(Fullscreen _) {
        remote.fullscreen();
    }

    @Subscribe
    public void onWindow(Window _) {
        remote.window();
    }

}
