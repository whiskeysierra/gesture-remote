package org.whiskeysierra.gestureremote.command.playback;

final class EventBuffer {

    private int updates;
    private float value;

    public float update(float delta) {
        updates++;
        value += delta;

        if (updates > 15) {
            try {
                return value;
            } finally {
                updates = 0;
                value = 0f;
            }
        } else {
            return Float.NaN;
        }
    }

}
