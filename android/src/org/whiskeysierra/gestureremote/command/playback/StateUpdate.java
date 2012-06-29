package org.whiskeysierra.gestureremote.command.playback;

public final class StateUpdate {

    private final State state;

    public StateUpdate(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }

}
