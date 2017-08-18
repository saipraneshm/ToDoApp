package com.codepath.preassignment.todoapp.database;

import java.util.Date;
import java.util.UUID;

/**
 * Created by saip92 on 8/7/2017.
 */

public class ToDoListItem {

    private String mTitle;
    private String mBody;
    private String mLastUpdated;
    private String mAssignedTo;
    private UUID mId;
    private String mDateCreated;

    //Automatically generates a unique sequence
    public ToDoListItem(){
        mId = UUID.randomUUID();
    }

    //Constructor to retrieve a todoList item
    public ToDoListItem(UUID id){
        mId = id;
    }


    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getBody() {
        return mBody;
    }

    public void setBody(String body) {
        mBody = body;
    }

    public String getLastUpdated() {
        return mLastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        mLastUpdated = lastUpdated;
    }

    public String getAssignedTo() {
        return mAssignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        mAssignedTo = assignedTo;
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public String getDateCreated() {
        return mDateCreated;
    }

    public void setDateCreated(String dateCreated) {
        mDateCreated = dateCreated;
    }
}
