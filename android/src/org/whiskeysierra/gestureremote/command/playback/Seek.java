package org.whiskeysierra.gestureremote.command.playback;

import org.whiskeysierra.gestureremote.command.Command;

public final class Seek implements Command {

    private final float percentage;

    public Seek(float percentage) {
        this.percentage = percentage;
    }

    public float getPercentage() {
        return percentage;
    }

}
