package org.whiskeysierra.gestureremote.server;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.IntentAction;
import org.nnsoft.guice.lifegycle.AfterInjection;
import org.whiskeysierra.R;
import org.whiskeysierra.R.id;
import org.whiskeysierra.R.string;
import org.whiskeysierra.gestureremote.FinishAction;
import org.whiskeysierra.gestureremote.server.model.Server;
import roboguice.activity.RoboListActivity;
import roboguice.inject.InjectView;

public class ServerListActivity extends RoboListActivity implements OnLongClickListener {

    @InjectView(id.actionbar)
    private ActionBar bar;

    @InjectView(android.R.id.list)
    private ListView list;

    @Inject
    private ServerManager manager;

    @Inject
    private EventBus bus;

    @AfterInjection
    public void onPostConstruct() {
        bus.register(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.servers);

        bar.setHomeAction(new FinishAction(this, R.drawable.ic_menu_back));
        bar.addAction(new IntentAction(this, new Intent(this, ServerActivity.class), R.drawable.ic_menu_add));
        bar.setTitle(string.servers);

        registerForContextMenu(list);
        list.setOnLongClickListener(this);

        updateList();
    }

    @Override
    protected void onListItemClick(ListView listView, View v, int position, long id) {
        listView.showContextMenuForChild(v);
        super.onListItemClick(listView, v, position, id);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        if (v.getId() == android.R.id.list) {
            menu.setHeaderTitle("Context Header Test");

            menu.add("Connect").setOnMenuItemClickListener(new OnMenuItemClickListener() {

                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    final int index = AdapterContextMenuInfo.class.cast(menuItem.getMenuInfo()).position;
                    Log.d(getClass().getName(), "Connecting to index " + index);
                    bus.post(new Connect(manager.findAt(index)));
                    return true;
                }

            });

            menu.add("Update").setOnMenuItemClickListener(new OnMenuItemClickListener() {

                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    final int index = AdapterContextMenuInfo.class.cast(menuItem.getMenuInfo()).position;
                    // TODO pass selected index to server activity
                    startActivityForResult(new Intent(ServerListActivity.this, ServerActivity.class), 0);
                    return true;
                }

            });

            menu.add("Delete").setOnMenuItemClickListener(new OnMenuItemClickListener() {

                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    final int index = AdapterContextMenuInfo.class.cast(menuItem.getMenuInfo()).position;
                    manager.delete(manager.findAt(index));
                    return true;
                }

            });
        }
    }

    /*
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;

        Toast.makeText(this, "Selected " + item.getTitle() + " (" + item.getItemId() + ") on " + index,
            Toast.LENGTH_LONG).show();

        return false;
    }
    */

    @Override
    public boolean onLongClick(View view) {
        closeContextMenu();
        return false;
    }

    private void updateList() {
        final Cursor cursor = manager.scroll();

        final ListAdapter adapter = new SimpleCursorAdapter(
            this, android.R.layout.simple_list_item_2, cursor,
            new String[]{SQLiteHelper.COLUMN_NAME, "address"},
            new int[]{android.R.id.text1, android.R.id.text2});

        setListAdapter(adapter);
    }

    @Subscribe
    public void onConnected(Connected connected) {
        bar.setTitle("Connected to " + connected.getServer().getName());
    }

    @Override
    protected void onResume() {
        updateList();
        super.onResume();
    }

}
