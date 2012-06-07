package org.whiskeysierra.gestureremote.servermanagment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.common.base.Strings;
import com.markupartist.android.widget.ActionBar;
import org.whiskeysierra.R;
import org.whiskeysierra.R.drawable;
import org.whiskeysierra.R.id;
import org.whiskeysierra.R.string;
import org.whiskeysierra.gestureremote.FinishAction;
import org.whiskeysierra.gestureremote.servermanagment.data.SettingsDAO;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class ServerActivity extends RoboActivity implements OnClickListener {

    @InjectView(id.newServerLayout)
    private LinearLayout layout;

    @InjectView(id.actionbar)
    private ActionBar bar;

    @InjectView(id.save)
    private Button save;

    private SettingsDAO dao;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_server);

        layout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                final InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
            }

        });

        bar.setHomeAction(new FinishAction(this, drawable.ic_menu_back));
        bar.setTitle(string.new_server);

        save.setOnClickListener(this);

        dao = new SettingsDAO(this);
        dao.open();
    }

    private EditText findEditTextById(int id) {
        return (EditText) findViewById(id);
    }

    private void resetForm(View view) {
        findEditTextById(R.id.svr_name).setText("");
        findEditTextById(R.id.svr_host).setText("");
        findEditTextById(R.id.svr_port).setText("");
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
            dao.createSetting(host, Integer.parseInt(port), name);
            resetForm(view);

            startActivity(new Intent(this, ServerListActivity.class));
        }
    }

    @Override
    protected void onResume() {
        dao.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        dao.close();
        super.onPause();
    }

}
