package org.whiskeysierra.gestureremote.recognition.gestures;

public final class HorizontalDrag implements TouchGesture {

    private final float distance;

    public HorizontalDrag(float distance) {
        this.distance = distance;
    }

    public float getDistance() {
        return distance;
    }

}
