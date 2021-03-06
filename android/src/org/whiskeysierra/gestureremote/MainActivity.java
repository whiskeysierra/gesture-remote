package org.whiskeysierra.gestureremote;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.common.base.Objects;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.markupartist.android.widget.ActionBar;
import org.nnsoft.guice.lifegycle.AfterInjection;
import org.whiskeysierra.R;
import org.whiskeysierra.R.drawable;
import org.whiskeysierra.R.id;
import org.whiskeysierra.gestureremote.command.playback.Fullscreen;
import org.whiskeysierra.gestureremote.command.playback.Pause;
import org.whiskeysierra.gestureremote.command.playback.Play;
import org.whiskeysierra.gestureremote.command.playback.Seek;
import org.whiskeysierra.gestureremote.command.playback.TurnVolume;
import org.whiskeysierra.gestureremote.command.playback.Window;
import org.whiskeysierra.gestureremote.command.playlist.Next;
import org.whiskeysierra.gestureremote.command.playlist.Previous;
import org.whiskeysierra.gestureremote.server.Connected;
import org.whiskeysierra.gestureremote.server.Disconnect;
import org.whiskeysierra.gestureremote.server.ServerListActivity;
import org.whiskeysierra.gestureremote.util.ChildActivityIntentAction;
import org.whiskeysierra.gestureremote.util.ViewSize;
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

    private String title;

    @AfterInjection
    public void onPostConstruct() {
        bus.register(this);
    }

    private void setTitle(String title) {
        this.title = title;
        bar.setTitle(title);
    }

    private void setStatus(String status) {
        text.setText(status);
    }

    private void setStatus(String pattern, Object... args) {
        setStatus(String.format(pattern, args));
    }

    private void setImage(int id) {
        if (id == 0) {
            image.setImageDrawable(null);
            image.setTag(null);
        } else {
            image.setImageResource(id);
            image.setTag(id);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        view.setOnTouchListener(this);
        view.post(this);

        bar.addAction(new ChildActivityIntentAction(this, ServerListActivity.class, R.drawable.ic_menu_preferences));

        setTitle("Not connected");
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        bundle.putString("title", title);
        bundle.putInt("image", (Integer) Objects.firstNonNull(image.getTag(), 0));
        bundle.putString("status", text.getText().toString());
        super.onSaveInstanceState(bundle);
    }

    @Override
    protected void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        setTitle(bundle.getString("title"));
        setImage(bundle.getInt("image"));
        setStatus(bundle.getString("status"));

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
    public void onConnected(Connected connected) {
        setTitle("Connected to " + connected.getServer().getName());
    }

    @Subscribe
    public void onDisconnect(Disconnect _) {
        setTitle("Disconnected");
    }

    @Subscribe
    public void onPlay(Play _) {
        setImage(R.drawable.play);
        setStatus("Playing");
    }

    @Subscribe
    public void onPause(Pause _) {
        setImage(R.drawable.pause);
        setStatus("Paused");
    }

    @Subscribe
    public void onNext(Next _) {
        setImage(R.drawable.forward_to_end);
        setStatus("Next!");
    }

    @Subscribe
    public void onPrevious(Previous _) {
        setImage(R.drawable.rewind_to_start);
        setStatus("Previous!");
    }

    @Subscribe
    public void onFullscreen(Fullscreen _) {
        setStatus("Fullscreen!");
    }

    @Subscribe
    public void onWindow(Window _) {
        setStatus("Window!");
    }

    @Subscribe
    public void onSeek(Seek seek) {
        if (seek.getPercentage() > 0f) {
            setImage(R.drawable.fast_forward);
        } else {
            setImage(R.drawable.rewind);
        }

        setStatus("Seeking");
    }

    @Subscribe
    public void onTurnVolume(TurnVolume volume) {
        final float percentage = volume.getPercentage();

        if (percentage > 0f) {
            setImage(drawable.up);
            setStatus("Turning volume up by %+.0f%%", percentage);
        } else {
            setImage(drawable.down);
            setStatus("Turning volume down by %+.0f%%", percentage);
        }
    }

}
