package org.whiskeysierra.gestureremote.server;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.google.inject.Inject;
import org.whiskeysierra.gestureremote.server.model.Server;
import roboguice.activity.event.OnCreateEvent;
import roboguice.activity.event.OnPauseEvent;
import roboguice.activity.event.OnResumeEvent;
import roboguice.event.Observes;

/**
 * Created with IntelliJ IDEA.
 * User: afeldmann
 * Date: 11.05.12
 * Time: 17:32
 * To change this template use File | Settings | File Templates.
 */
public final class ServerManager {

    @Inject
    private SQLiteHelper helper;

    private SQLiteDatabase database;

    public void onCreate(@Observes OnCreateEvent _) {
        open();
    }

    public void onResume(@Observes OnResumeEvent _) {
        open();
    }

    public void onPause(@Observes OnPauseEvent _) {
        close();
    }

    private void open() {
        database = helper.getWritableDatabase();
    }

    private SQLiteDatabase get() {
        if (database == null) {
            open();
        }
        return database;
    }

    private void close() {
        if (database != null) {
            database.close();
            database = null;
        }
    }

    public void create(Server server) {
        final ContentValues values = toValues(server);

        final long insertID = get().insert(SQLiteHelper.TABLE_SETTINGS, null, values);
        server.setId(insertID);
    }

    public void update(Server server) {
        final ContentValues values = toValues(server);
        get().update(SQLiteHelper.TABLE_SETTINGS, values, SQLiteHelper.COLUMN_ID + "=" + server.getId(), null);
    }

    private ContentValues toValues(Server server) {
        final ContentValues values = new ContentValues();
        values.put(SQLiteHelper.COLUMN_IP, server.getHost());
        values.put(SQLiteHelper.COLUMN_PORT, server.getPort());
        values.put(SQLiteHelper.COLUMN_SELECTED, server.isSelected());
        values.put(SQLiteHelper.COLUMN_NAME, server.getName());
        return values;
    }

    public void delete(Server server) {
        get().delete(SQLiteHelper.TABLE_SETTINGS, SQLiteHelper.COLUMN_ID + "=" + server.getId(), null);
    }

    public Server findAt(int index) {
        final Server server = new Server();

        final String sql =
            "SELECT " +
                SQLiteHelper.COLUMN_ID + ", " +
                SQLiteHelper.COLUMN_NAME + ", " +
                SQLiteHelper.COLUMN_IP +  ", " +
                SQLiteHelper.COLUMN_PORT + " " +
            "FROM " +
                SQLiteHelper.TABLE_SETTINGS + " " +
            "LIMIT " +
                "1 " +
            "OFFSET " +
                index;

        final Cursor cursor = get().rawQuery(sql, null);

        cursor.moveToFirst();

        server.setId(cursor.getLong(0));
        server.setName(cursor.getString(1));
        server.setHost(cursor.getString(2));
        server.setPort(cursor.getInt(3));

        return server;
    }

    public Cursor scroll() {
        final String sql =
            "SELECT " +
                SQLiteHelper.COLUMN_ID + ", " +
                SQLiteHelper.COLUMN_NAME + ", " +
                "(" + SQLiteHelper.COLUMN_IP +  " || ':' || " + SQLiteHelper.COLUMN_PORT + ") AS address " +
            "FROM " +
                SQLiteHelper.TABLE_SETTINGS;

        return get().rawQuery(sql, null);
    }
}
