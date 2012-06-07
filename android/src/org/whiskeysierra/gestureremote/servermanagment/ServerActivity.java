package org.whiskeysierra.gestureremote.servermanagment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.IntentAction;
import org.whiskeysierra.R;
import org.whiskeysierra.R.id;
import org.whiskeysierra.R.string;
import org.whiskeysierra.gestureremote.FinishAction;
import org.whiskeysierra.gestureremote.MainActivity;
import org.whiskeysierra.gestureremote.servermanagment.data.SQLiteHelper;
import org.whiskeysierra.gestureremote.servermanagment.data.SettingsDAO;
import roboguice.activity.RoboListActivity;
import roboguice.inject.InjectView;

public class ServerActivity extends RoboListActivity implements OnLongClickListener {

    @InjectView(id.actionbar)
    private ActionBar bar;

    @InjectView(android.R.id.list)
    private ListView list;

    private SettingsDAO dao;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.servers);

        bar.setHomeAction(new FinishAction(this, R.drawable.ic_menu_back));
        bar.addAction(new IntentAction(this, new Intent(this, NewServerActivity.class), R.drawable.ic_menu_add));
        bar.setTitle(string.servers);

        registerForContextMenu(list);
        list.setOnLongClickListener(this);

        dao = new SettingsDAO(this);
        dao.open();

        updateList();
    }

    @Override
    protected void onListItemClick(ListView listView, View v, int position, long id) {
        openContextMenu(listView);
        super.onListItemClick(listView, v, position, id);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        if (v.getId() == android.R.id.list) {
            menu.setHeaderTitle("Context Header Test");

            menu.add("Connect");
            menu.add("Update");
            menu.add("Delete");
        }
    }

    @Override
    public boolean onLongClick(View view) {
        closeContextMenu();
        return false;
    }

    private void updateList() {
        final Cursor cursor = dao.scroll();

        final ListAdapter adapter = new SimpleCursorAdapter(
            this, android.R.layout.simple_list_item_2, cursor,
            new String[]{SQLiteHelper.COLUMN_NAME, "address"},
            new int[]{android.R.id.text1, android.R.id.text2});

        setListAdapter(adapter);
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
