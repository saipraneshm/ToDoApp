package com.codepath.preassignment.todoapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.codepath.preassignment.todoapp.database.ToDoListContract.ToDoListEntry.ToDoListTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by saip92 on 8/8/2017.
 */

public class ToDoListDB {

    private static ToDoListDB sToDoListDB;
    private Context mContext;
    private SQLiteDatabase mDatabase;
    private static final String TAG = ToDoListDB.class.getSimpleName();

    private ToDoListDB(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new ToDoListDBHelper(mContext).getWritableDatabase();
    }

    public static ToDoListDB get(Context context){
        if(sToDoListDB == null){
            sToDoListDB = new ToDoListDB(context);
        }
        return sToDoListDB;
    }

    private static ContentValues getContentValues(ToDoListItem item){
        ContentValues contentValues = new ContentValues();

        contentValues.put(ToDoListTable.COLS.UUID, String.valueOf(item.getId()));
        contentValues.put(ToDoListTable.COLS.TITLE, item.getTitle());
        contentValues.put(ToDoListTable.COLS.BODY, item.getBody());
        contentValues.put(ToDoListTable.COLS.ASSIGNED_TO, item.getAssignedTo());
        contentValues.put(ToDoListTable.COLS.DATE_CREATED, item.getDateCreated());
        contentValues.put(ToDoListTable.COLS.DATE_UPDATED, item.getLastUpdated());
        contentValues.put(ToDoListTable.COLS.DUE_DATE, item.getDueDate().getTime());
        contentValues.put(ToDoListTable.COLS.PRIORITY, item.getPriority());
        contentValues.put(ToDoListTable.COLS.TASK_STATUS, item.isTaskDone() ? 1 : 0);

        return contentValues;
    }

    public void addItem(ToDoListItem item){
        ContentValues contentValues = getContentValues(item);
        mDatabase.insert(ToDoListTable.NAME, null, contentValues);
        //Log.d(TAG,"Result for inserting into the database : " + result);
    }

    public void deleteItem(UUID mId){
        int result = mDatabase.delete(ToDoListTable.NAME, ToDoListTable.COLS.UUID + " = ?",
                new String[]{mId.toString()});
        Log.d(TAG, "Result of deleting a record: " + result);
    }

    private ToDoListCursorWrapper queryToDoListItems(String where ,String[] whereArgs){
        Cursor cursor = mDatabase.query(ToDoListTable.NAME,
                null,
                where,
                whereArgs,
                null,
                null,
                ToDoListTable.COLS.DATE_CREATED);
        return new ToDoListCursorWrapper(cursor);
    }

    public void updateItem(ToDoListItem item){
        String uuidString = item.getId().toString();
        ContentValues values = getContentValues(item);

        mDatabase.update(ToDoListTable.NAME, values, ToDoListTable.COLS.UUID + " = ?",
                new String[]{ uuidString});
    }



    public List<ToDoListItem> getAllItems(){
        List<ToDoListItem> items = new ArrayList<>();

        ToDoListCursorWrapper cursor = queryToDoListItems(null, null);
        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                items.add(cursor.getToDoListItem());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }
        return items;
    }

    public ToDoListItem getItem(UUID uuid){
        String uuidString = uuid.toString();
        ToDoListCursorWrapper cursor = queryToDoListItems( ToDoListTable.COLS.UUID + "= ?",
                new String[]{uuidString});
        try{
            if(cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();
            return cursor.getToDoListItem();
        }finally {
            cursor.close();
        }
    }




}
