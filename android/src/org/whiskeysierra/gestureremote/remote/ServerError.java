package org.whiskeysierra.gestureremote.remote;

public final class ServerError {

    private final String message;

    public ServerError(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }

}
