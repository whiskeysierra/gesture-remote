package org.whiskeysierra.gestureremote.servermanagment.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: afeldmann
 * Date: 11.05.12
 * Time: 17:12
 * To change this template use File | Settings | File Templates.
 */
public class SQLiteHelper extends SQLiteOpenHelper{

    public static final String TABLE_SETTINGS = "settings";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_IP = "setting";
    public static final String COLUMN_PORT = "port";
    public static final String COLUMN_SELECTED = "selected";
    public static final String COLUMN_NAME = "name";

    private static final String DATABASE_NAME = "settings.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DB_CREATE = "CREATE TABLE "
            + TABLE_SETTINGS + "(" + COLUMN_ID
            + " integer primary key autoincrement, "
            + COLUMN_IP + " text not null, "
            + COLUMN_PORT + " integer not null, "
            + COLUMN_SELECTED + " integer not null, "
            + COLUMN_NAME + " text not null);";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(DB_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        Log.w(SQLiteHelper.class.getName(),
                "Upgrading DB from Verison;" + oldVersion + " to " + newVersion);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);
        onCreate(db);

    }



}
