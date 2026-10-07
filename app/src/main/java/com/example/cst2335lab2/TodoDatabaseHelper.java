
package com.example.cst2335lab2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TodoDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "TodoDatabase.db";
    public static final int VERSION_NUM = 1;

    public static final String TABLE_NAME = "Todos";
    public static final String COL_ID = "_id";
    public static final String COL_TODO = "TODO";
    public static final String COL_URGENT = "URGENT";

    public TodoDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL_TODO + " TEXT NOT NULL, " +
                        COL_URGENT + " INTEGER NOT NULL)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
