package org.whiskeysierra.gestureremote.command.playback;

enum State {

    INITIAL {

        @Override
        public State next() {
            return PLAYING;
        }

    },

    PLAYING {

        @Override
        public State next() {
            return PAUSED;
        }

    },

    PAUSED {

        @Override
        public State next() {
            return PLAYING;
        }

    };

    public abstract State next();
}
