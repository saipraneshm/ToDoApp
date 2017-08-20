package com.codepath.preassignment.todoapp.database;

import android.os.Parcel;
import android.os.Parcelable;

import com.codepath.preassignment.todoapp.Priority;

import java.util.Date;
import java.util.UUID;

/**
 * Created by saip92 on 8/7/2017.
 */

public class ToDoListItem implements Parcelable {

    private String mTitle;
    private String mBody;
    private String mLastUpdated;
    private String mAssignedTo;
    private UUID mId;
    private String mDateCreated;
    private String mDueDate;


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

    public String getDueDate() {
        return mDueDate;
    }

    public void setDueDate(String dueDate) {
        mDueDate = dueDate;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mTitle);
        dest.writeString(this.mBody);
        dest.writeString(this.mLastUpdated);
        dest.writeString(this.mAssignedTo);
        dest.writeSerializable(this.mId);
        dest.writeString(this.mDateCreated);
        dest.writeString(this.mDueDate);
    }

    protected ToDoListItem(Parcel in) {
        this.mTitle = in.readString();
        this.mBody = in.readString();
        this.mLastUpdated = in.readString();
        this.mAssignedTo = in.readString();
        this.mId = (UUID) in.readSerializable();
        this.mDateCreated = in.readString();
        this.mDueDate = in.readString();
    }

    public static final Parcelable.Creator<ToDoListItem> CREATOR = new Parcelable.Creator<ToDoListItem>() {
        @Override
        public ToDoListItem createFromParcel(Parcel source) {
            return new ToDoListItem(source);
        }

        @Override
        public ToDoListItem[] newArray(int size) {
            return new ToDoListItem[size];
        }
    };
}
