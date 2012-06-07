package org.whiskeysierra.gestureremote;

import android.R.drawable;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;
import com.markupartist.android.widget.ActionBar.IntentAction;
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

    @InjectView(id.actionbar)
    private ActionBar bar;

    @InjectView(id.image)
    private ImageView image;

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

        bar.addAction(new ChildActivityIntentAction(this, ServerActivity.class, R.drawable.ic_menu_preferences));

        text.setText("Not connected");
    }

    @Override
    public void run() {
        bus.post(new ViewSize(view.getWidth(), view.getHeight()));
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        bus.post(event);
        return true;
    }

    @Subscribe
    public void onPlay(Play _) {
        image.setImageResource(R.drawable.play);
        text.setText("Playing");
    }

    @Subscribe
    public void onPause(Pause _) {
        image.setImageResource(R.drawable.pause);
        text.setText("Paused.");
    }

    @Subscribe
    public void onNext(Next _) {
        image.setImageResource(R.drawable.forward_to_end);
        text.setText("Next!");
    }

    @Subscribe
    public void onPrevious(Previous _) {
        image.setImageResource(R.drawable.rewind_to_start);
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
        if (seek.getPercentage() > 0) {
            image.setImageResource(R.drawable.fast_forward);
        } else {
            image.setImageResource(R.drawable.rewind);
        }

        text.setText("Seeking " + seek.getPercentage());
    }

    @Subscribe
    public void onTurnVolume(TurnVolume volume) {
        image.setImageDrawable(null);
        text.setText("Setting volume " + volume.getPercentage());
    }

}
