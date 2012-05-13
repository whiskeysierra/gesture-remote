package org.whiskeysierra.gestureremote.servermanagment;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import org.whiskeysierra.R;
import org.whiskeysierra.gestureremote.servermanagment.data.Settings;
import org.whiskeysierra.gestureremote.servermanagment.data.SettingsDAO;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: afeldmann
 * Date: 11.05.12
 * Time: 21:11
 * To change this template use File | Settings | File Templates.
 */
public class ServerActivity extends ListActivity {
    private SettingsDAO dao;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.servers);

        dao = new SettingsDAO(this);
        dao.open();
        updateList();
    }

    private void updateList() {
        List<Settings> values = dao.getAllSettings();
        ArrayAdapter<Settings> adapter = new ArrayAdapter<Settings>(this,
                android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
    }

    public void newServer(View view){
        Intent intent = new Intent(view.getContext(), NewServerActivity.class);
        startActivityForResult(intent,0);
    }

    @Override
    protected void onResume() {
        dao.open();
        updateList();
        super.onResume();
    }

    @Override
    protected void onPause() {
        dao.close();
        super.onPause();
    }
}
