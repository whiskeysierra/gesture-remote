package org.whiskeysierra.gestureremote.command.playback;

import org.whiskeysierra.gestureremote.command.Command;

public final class TurnVolume implements Command {

    private final float percentage;

    public TurnVolume(float percentage) {
        this.percentage = percentage;
    }

    public float getPercentage() {
        return percentage;
    }

}
