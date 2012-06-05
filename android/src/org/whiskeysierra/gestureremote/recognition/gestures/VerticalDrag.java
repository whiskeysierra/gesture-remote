package org.whiskeysierra.gestureremote.recognition.gestures;

public final class VerticalDrag implements TouchGesture {

    private final float distance;

    public VerticalDrag(float distance) {
        this.distance = distance;
    }

    public float getDistance() {
        return distance;
    }

}
