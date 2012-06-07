package org.whiskeysierra.gestureremote.servermanagment.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: afeldmann
 * Date: 11.05.12
 * Time: 17:32
 * To change this template use File | Settings | File Templates.
 */
public class SettingsDAO {

    private SQLiteDatabase db;
    private SQLiteHelper helper;
    private String[] allSettings = {SQLiteHelper.COLUMN_ID, SQLiteHelper.COLUMN_IP, SQLiteHelper.COLUMN_PORT, SQLiteHelper.COLUMN_SELECTED, SQLiteHelper.COLUMN_NAME};

    public SettingsDAO(Context context) {
        helper = new SQLiteHelper(context);
    }

    public void open() throws SQLException {
        db = helper.getWritableDatabase();
    }

    public void close() {
        db.close();
    }

    public Settings createSetting(String host, int port, String name) {
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.COLUMN_IP, host);
        values.put(SQLiteHelper.COLUMN_PORT, port);
        values.put(SQLiteHelper.COLUMN_SELECTED, false);
        values.put(SQLiteHelper.COLUMN_NAME, name);

        long insertID = db.insert(SQLiteHelper.TABLE_SETTINGS, null, values);
        Cursor cursor = db.query(SQLiteHelper.TABLE_SETTINGS, allSettings, SQLiteHelper.COLUMN_ID + " = " + insertID, null, null, null, null);
        cursor.moveToFirst();
        Settings newSettings = cursorToSettings(cursor);
        cursor.close();
        Log.w("Db-Test:Obj", newSettings.toString());
        return newSettings;
    }

    public void deleteSettings(Settings settings) {
        long id = settings.getId();
        System.out.println("Settings id deleted:" + id);
        db.delete(SQLiteHelper.TABLE_SETTINGS, SQLiteHelper.COLUMN_ID + " = " + id, null);
    }

    public Cursor scroll() {
        final String sql =
            "SELECT " +
                SQLiteHelper.COLUMN_ID + ", " +
                SQLiteHelper.COLUMN_NAME + ", " +
                "(" + SQLiteHelper.COLUMN_IP +  " || ':' || " + SQLiteHelper.COLUMN_PORT + ") AS address " +
            "FROM " +
                "settings";
        return db.rawQuery(sql, null);
    }

    public List<Settings> getAllSettings() {
        List<Settings> settings = new ArrayList<Settings>();

        Cursor cursor = db.query(SQLiteHelper.TABLE_SETTINGS, allSettings, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Settings setting = cursorToSettings(cursor);
            settings.add(setting);
            cursor.moveToNext();
        }
        cursor.close();
        return settings;
    }

    private Settings cursorToSettings(Cursor cursor) {
        Settings settings = new Settings();
        settings.setId(cursor.getLong(0));
        settings.setHost(cursor.getString(1));
        settings.setPort(cursor.getInt(2));
        settings.setSelected(cursor.getInt(3) == 1);
        settings.setName(cursor.getString(4));
        return settings;
    }


}
