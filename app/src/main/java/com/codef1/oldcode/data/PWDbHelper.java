package com.codef1.oldcode.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yuzhu on 2017/12/15.
 */

public class PWDbHelper extends SQLiteOpenHelper {

    public PWDbHelper(Context context){
        this(context, PasswordContract.DATABASE_NAME, null, PasswordContract.DATABASE_VERSION);
    }
    public PWDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + PasswordContract.PWEntry.TABLE + " ( " +
                PasswordContract.PWEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PasswordContract.PWEntry.COL_PW_NAME + " TEXT NOT NULL, " +
                PasswordContract.PWEntry.COL_PW_VALUE + " INTEGER NOT NULL);";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PasswordContract.PWEntry.TABLE);
        onCreate(db);
    }
}
