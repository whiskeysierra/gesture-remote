package org.whiskeysierra.gestureremote;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import org.nnsoft.guice.lifegycle.AfterInjection;
import org.whiskeysierra.R;
import org.whiskeysierra.R.id;
import org.whiskeysierra.gestureremote.command.playback.Fullscreen;
import org.whiskeysierra.gestureremote.command.playback.Pause;
import org.whiskeysierra.gestureremote.command.playback.Play;
import org.whiskeysierra.gestureremote.command.playback.Seek;
import org.whiskeysierra.gestureremote.command.playback.TurnVolume;
import org.whiskeysierra.gestureremote.command.playback.Window;
import org.whiskeysierra.gestureremote.command.playlist.Next;
import org.whiskeysierra.gestureremote.command.playlist.Previous;
import org.whiskeysierra.gestureremote.servermanagment.ServerActivity;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class MainActivity extends RoboActivity implements OnTouchListener, Runnable {

    @Inject
    private EventBus bus;

    @InjectView(id.gestures)
    private View view;

    @InjectView(id.text)
    private TextView text;

    @AfterInjection
    public void onPostConstruct() {
        bus.register(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        view.setOnTouchListener(this);
        view.post(this);
    }

    @Override
    public void run() {
        bus.post(new ViewSize(view.getWidth(), view.getHeight()));
    }

    public void showServerSettings(View view){
        startActivityForResult(new Intent(view.getContext(), ServerActivity.class), 0);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        bus.post(event);
        return true;
    }

    @Subscribe
    public void onPlay(Play _) {
        text.setText("Playing...");
    }

    @Subscribe
    public void onPause(Pause _) {
        text.setText("Paused.");
    }

    @Subscribe
    public void onNext(Next _) {
        text.setText("Next!");
        showServerSettings(view);
    }

    @Subscribe
    public void onPrevious(Previous _) {
        text.setText("Previous!");
    }

    @Subscribe
    public void onFullscreen(Fullscreen _) {
        text.setText("Fullscreen!");
    }

    @Subscribe
    public void onWindow(Window _) {
        text.setText("Window!");
    }

    @Subscribe
    public void onSeek(Seek seek) {
        text.setText("Seeking " + seek.getPercentage());
    }

    @Subscribe
    public void onTurnVolume(TurnVolume volume) {
        text.setText("Setting volume " + volume.getPercentage());
    }

}
