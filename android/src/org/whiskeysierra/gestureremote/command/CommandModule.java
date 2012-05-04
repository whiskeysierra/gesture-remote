package org.whiskeysierra.gestureremote.command;

import com.google.inject.AbstractModule;
import org.whiskeysierra.gestureremote.command.playback.PlaybackModule;
import org.whiskeysierra.gestureremote.command.playlist.PlaylistModule;
import org.whiskeysierra.gestureremote.command.volume.VolumeModule;

public final class CommandModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new PlaybackModule());
        install(new PlaylistModule());
        install(new VolumeModule());
    }

}
