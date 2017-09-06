package com.codepath.preassignment.todoapp.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.codepath.preassignment.todoapp.database.ToDoListContract.ToDoListEntry.ToDoListTable;

import java.util.Date;
import java.util.UUID;

/**
 * Created by saip92 on 8/8/2017.
 */

public class ToDoListCursorWrapper extends CursorWrapper {

    public ToDoListCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public ToDoListItem getToDoListItem(){
        String uuid = getString(getColumnIndex(ToDoListTable.COLS.UUID));
        String title = getString(getColumnIndex(ToDoListTable.COLS.TITLE));
        String body = getString(getColumnIndex(ToDoListTable.COLS.BODY));
        String assignedTo = getString(getColumnIndex(ToDoListTable.COLS.ASSIGNED_TO));
        String dateCreated = getString(getColumnIndex(ToDoListTable.COLS.DATE_CREATED));
        String lastUpdated = getString(getColumnIndex(ToDoListTable.COLS.DATE_UPDATED));
        long dueDate = getLong(getColumnIndex(ToDoListTable.COLS.DUE_DATE));
        int priority = getInt(getColumnIndex(ToDoListTable.COLS.PRIORITY));
        boolean taskStatus = getInt(getColumnIndex(ToDoListTable.COLS.TASK_STATUS)) != 0;
        boolean reminder = getInt(getColumnIndex(ToDoListTable.COLS.REMINDER)) != 0;

        ToDoListItem item = new ToDoListItem();
        item.setId(UUID.fromString(uuid));
        item.setTitle(title);
        item.setBody(body);
        item.setAssignedTo(assignedTo);
        item.setDateCreated(dateCreated);
        item.setLastUpdated(lastUpdated);
        item.setDueDate(new Date(dueDate));
        item.setPriority(priority);
        item.setTaskStatus(taskStatus);
        item.setReminder(reminder);

        return item;
    }
}
