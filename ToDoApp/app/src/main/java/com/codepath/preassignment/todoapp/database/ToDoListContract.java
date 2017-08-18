package com.codepath.preassignment.todoapp.database;

import android.provider.BaseColumns;

/**
 * Created by saip92 on 8/7/2017.
 */

public final class ToDoListContract {

    private ToDoListContract(){}

    public static class ToDoListEntry{

        public static class ToDoListTable implements BaseColumns{
            public static final String NAME = "todoListTable";

            public static class COLS {
                public static final String UUID = "uuid";
                public static final String TITLE = "title";
                public static final String BODY = "body";
                public static final String ASSIGNED_TO  = "assignedTo";
                public static final String DATE_CREATED = "dateCreated";
                public static final String DATE_UPDATED = "dateUpdated";
            }
        }
    }
}
