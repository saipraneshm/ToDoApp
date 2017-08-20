package com.codepath.preassignment.todoapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.codepath.preassignment.todoapp.database.ToDoListContract.ToDoListEntry.ToDoListTable;

/**
 * Created by saip92 on 8/7/2017.
 */

public class ToDoListDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "todolist.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TAG = ToDoListDBHelper.class.getSimpleName();


    public ToDoListDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_TODO_TABLE = " CREATE TABLE " + ToDoListTable.NAME + " ( " +
                ToDoListTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ToDoListTable.COLS.UUID + " TEXT NOT NULL ," +
                ToDoListTable.COLS.TITLE + " TEXT , " +
                ToDoListTable.COLS.BODY + " TEXT NOT NULL," +
                ToDoListTable.COLS.ASSIGNED_TO + " TEXT," +
                ToDoListTable.COLS.DATE_CREATED + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                ToDoListTable.COLS.DATE_UPDATED + " TEXT, " +
                ToDoListTable.COLS.DUE_DATE  + " TEXT, " +
                ToDoListTable.COLS.PRIORITY  + " INTEGER " +
                ");";
        Log.d(TAG, CREATE_TODO_TABLE);

        sqLiteDatabase.execSQL(CREATE_TODO_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ToDoListTable.NAME);
        onCreate(sqLiteDatabase);
    }
}
