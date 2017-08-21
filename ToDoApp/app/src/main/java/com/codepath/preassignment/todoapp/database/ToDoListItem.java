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
    private Date mDueDate;
    private int mPriority;
    private boolean mTaskStatus;


    //Automatically generates a unique sequence
    public ToDoListItem(){
        mId = UUID.randomUUID();
        mTaskStatus = false;
        mDueDate = new Date();
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



    public int getPriority() {
        return mPriority;
    }

    public void setPriority(int priority) {
        mPriority = priority;
    }


    public boolean isTaskDone() {
        return mTaskStatus;
    }

    public void setTaskStatus(boolean taskStatus) {
        mTaskStatus = taskStatus;
    }

    public Date getDueDate() {
        return mDueDate;
    }

    public void setDueDate(Date dueDate) {
        mDueDate = dueDate;
    }


    @Override
    public String toString() {
        return "ToDoListItem{" +
                "mTitle='" + mTitle + '\'' +
                ", mBody='" + mBody + '\'' +
                ", mLastUpdated='" + mLastUpdated + '\'' +
                ", mAssignedTo='" + mAssignedTo + '\'' +
                ", mId=" + mId +
                ", mDateCreated='" + mDateCreated + '\'' +
                ", mDueDate='" + mDueDate + '\'' +
                ", mPriority=" + mPriority +
                '}';
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
        dest.writeLong(this.mDueDate != null ? this.mDueDate.getTime() : -1);
        dest.writeInt(this.mPriority);
        dest.writeByte(this.mTaskStatus ? (byte) 1 : (byte) 0);
    }

    protected ToDoListItem(Parcel in) {
        this.mTitle = in.readString();
        this.mBody = in.readString();
        this.mLastUpdated = in.readString();
        this.mAssignedTo = in.readString();
        this.mId = (UUID) in.readSerializable();
        this.mDateCreated = in.readString();
        long tmpMDueDate = in.readLong();
        this.mDueDate = tmpMDueDate == -1 ? null : new Date(tmpMDueDate);
        this.mPriority = in.readInt();
        this.mTaskStatus = in.readByte() != 0;
    }

    public static final Creator<ToDoListItem> CREATOR = new Creator<ToDoListItem>() {
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
