package org.whiskeysierra.gestureremote.server;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.markupartist.android.widget.ActionBar;
import org.whiskeysierra.R;
import org.whiskeysierra.R.drawable;
import org.whiskeysierra.R.id;
import org.whiskeysierra.R.string;
import org.whiskeysierra.gestureremote.FinishAction;
import org.whiskeysierra.gestureremote.server.model.Server;
import org.whiskeysierra.gestureremote.util.KeyboardHider;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class ServerActivity extends RoboActivity implements OnClickListener {

    @InjectView(id.newServerLayout)
    private LinearLayout layout;

    @InjectView(id.actionbar)
    private ActionBar bar;

    @InjectView(id.save)
    private Button save;

    @Inject
    private ServerManager manager;

    private Server server;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_server);

        layout.setOnClickListener(new KeyboardHider());

        bar.setHomeAction(new FinishAction(this, drawable.ic_menu_back));
        bar.setTitle(string.new_server);

        save.setOnClickListener(this);

        final Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            final int index = bundle.getInt("index");

            server = manager.findAt(index);

            findEditTextById(R.id.svr_name).setText(server.getName());
            findEditTextById(R.id.svr_host).setText(server.getHost());
            findEditTextById(R.id.svr_port).setText(Integer.toString(server.getPort()));
        }
    }

    private EditText findEditTextById(int id) {
        return (EditText) findViewById(id);
    }

    private void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        final String name = findEditTextById(R.id.svr_name).getText().toString();
        final String host = findEditTextById(R.id.svr_host).getText().toString();
        final String port = findEditTextById(R.id.svr_port).getText().toString();

        if (Strings.isNullOrEmpty(host)) {
            toast("Please input a valid address");
        } else if (Strings.isNullOrEmpty(port) || Integer.parseInt(port) > 65536) {
            toast("Please input a valid port");
        } else if (name.length() == 0) {
            toast("Please input a valid name");
        } else {
            if (server == null) {
                server = new Server();
            }

            server.setHost(host);
            server.setPort(Integer.parseInt(port));
            server.setName(name);

            if (server.getId() == 0) {
                manager.create(server);
            } else {
                manager.update(server);
            }

            finish();
        }
    }

}
