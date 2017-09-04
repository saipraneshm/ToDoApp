package com.codepath.preassignment.todoapp.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import com.codepath.preassignment.todoapp.R;
import com.codepath.preassignment.todoapp.activity.ToDoListActivity;
import com.codepath.preassignment.todoapp.database.ToDoListDB;
import com.codepath.preassignment.todoapp.database.ToDoListItem;
import com.codepath.preassignment.todoapp.utils.Priority;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created by saip92 on 9/4/2017.
 */

public class TaskReminderService extends IntentService {

    private static final String TAG = TaskReminderService.class.getSimpleName();
    private ToDoListDB mDB;
    private static final String TASK_ID = "TaskReminderService.taskId";

    public TaskReminderService() {
        super(TAG);
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, TaskReminderService.class);
    }

    public static void setTaskReminder(Context context, ToDoListItem item) {
        Log.d(TAG, item.toString());
        int keyForReminder = Integer.parseInt(item.getDateCreated());
        Intent i = TaskReminderService.newIntent(context);
        i.putExtra(TASK_ID, item.getId());
        PendingIntent pi = PendingIntent.getService(context, keyForReminder, i, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(item.getDueDate());

        int ALARM_TYPE = AlarmManager.RTC_WAKEUP;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.d(TAG, "Setting exact");
            alarmManager.setExact(ALARM_TYPE, calendar.getTimeInMillis(), pi);
        } else {
            Log.d(TAG, "Calling set");
            alarmManager.set(ALARM_TYPE, calendar.getTimeInMillis(), pi);
        }

    }

    public static void cancelTaskReminder(Context context, ToDoListItem item) {
        if (item.getDateCreated() != null) {
            int keyForReminder = Integer.parseInt(item.getDateCreated());
            Intent i = TaskReminderService.newIntent(context);
            PendingIntent pi = PendingIntent.getService(context, keyForReminder, i,
                    PendingIntent.FLAG_NO_CREATE);

            if(pi != null){
                AlarmManager alarmManager = (AlarmManager) context.getSystemService
                        (Context.ALARM_SERVICE);
                NotificationManagerCompat notificationManager = NotificationManagerCompat
                        .from(context);

                alarmManager.cancel(pi);
                pi.cancel();
                notificationManager.cancel(keyForReminder);
            }
        }
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        mDB = ToDoListDB.get(this);


        if(intent != null){
            UUID taskId = (UUID) intent.getSerializableExtra(TASK_ID);
            if(taskId != null){
                ToDoListItem item = mDB.getItem(taskId);
                Log.d(TAG, item.toString());
                if(item.getDateCreated() != null){
                    int key = Integer.parseInt(item.getDateCreated());
                    Resources resources = getResources();
                    Intent i = ToDoListActivity.newIntent(this,taskId);
                    PendingIntent pi = PendingIntent.getActivity(this,
                            key, i, 0);

                    Notification notification = new NotificationCompat.Builder(this)
                            .setTicker(Priority.getString(item.getPriority()))
                            .setSmallIcon(android.R.drawable.ic_menu_report_image)
                            .setContentTitle(resources.getString(R.string.task_reminder_title))
                            .setContentText(item.getTitle())
                            .setContentIntent(pi)
                            .setAutoCancel(true)
                            .build();

                    NotificationManagerCompat notificationManager = NotificationManagerCompat
                            .from(this);

                    notificationManager.notify(key, notification);
                }

            }
        }

       // Log.d(TAG,"Caught intent: " + intent);
    }
}
