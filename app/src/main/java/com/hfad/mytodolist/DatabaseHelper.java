package com.hfad.mytodolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static int DB_VERSION = 1;
    public static String DB_NAME = "database";
    DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        updateMyDatabase(db,0,DB_VERSION);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldversion,int newversion)
    {
        updateMyDatabase(db,oldversion,newversion);
    }
    public void updateMyDatabase(SQLiteDatabase db,int oldversion,int newversion)
    {
        if (oldversion < 1) {
            db.execSQL("CREATE TABLE TODOLIST(" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "TASK TEXT)");
        }
        if(oldversion < 2){
            db.execSQL("ALTER TABLE TODOLIST ADD COLUMN DESCRIPTION");
        }
    }
}
