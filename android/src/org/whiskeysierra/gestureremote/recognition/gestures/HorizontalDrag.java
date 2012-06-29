package org.whiskeysierra.gestureremote.recognition.gestures;

public final class HorizontalDrag implements TouchGesture {

    private final float x;

    public HorizontalDrag(float x) {
        this.x = x;
    }

    public float getX() {
        return x;
    }

}
