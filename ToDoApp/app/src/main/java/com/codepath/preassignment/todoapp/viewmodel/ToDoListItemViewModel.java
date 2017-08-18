package com.codepath.preassignment.todoapp.viewmodel;

import android.databinding.BaseObservable;
import android.text.TextUtils;

import com.codepath.preassignment.todoapp.database.ToDoListDB;
import com.codepath.preassignment.todoapp.database.ToDoListItem;

/**
 * Created by saip92 on 8/8/2017.
 */

public class ToDoListItemViewModel extends BaseObservable {


    private ToDoListItem mToDoListItem;
    private ToDoListDB mToDoListDB;

    public ToDoListItemViewModel(ToDoListDB db){
        mToDoListDB = db;
    }

    public ToDoListItem getToDoListItem() {
        return mToDoListItem;
    }

    public void setToDoListItem(ToDoListItem toDoListItem) {
        mToDoListItem = toDoListItem;
    }

    public String getTitle(){
        if(mToDoListItem.getTitle() != null && !TextUtils.isEmpty(mToDoListItem.getTitle()) ){
            return mToDoListItem.getTitle();
        }else{
            return mToDoListItem.getBody();
        }
    }
}
