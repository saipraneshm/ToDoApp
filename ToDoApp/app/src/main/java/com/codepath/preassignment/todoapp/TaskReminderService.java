package com.codepath.preassignment.todoapp;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by saip92 on 9/4/2017.
 */

public class TaskReminderService extends IntentService {

    private static final String TAG = TaskReminderService.class.getSimpleName();

    public TaskReminderService() {
        super(TAG);
    }

    public static Intent newIntent(Context context){
        return new Intent(context, TaskReminderService.class);
    }

    public static void setTaskReminder(Context context){
        Intent i = TaskReminderService.newIntent(context);
        PendingIntent pi = PendingIntent.getService(context, (int) System.currentTimeMillis(),i,0);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
