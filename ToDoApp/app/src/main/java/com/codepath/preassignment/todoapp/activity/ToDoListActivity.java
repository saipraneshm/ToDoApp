package com.codepath.preassignment.todoapp.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.codepath.preassignment.todoapp.R;
import com.codepath.preassignment.todoapp.activity.abs.SingleFragmentActivity;
import com.codepath.preassignment.todoapp.fragments.ToDoListFragment;

import java.util.UUID;


public class ToDoListActivity extends SingleFragmentActivity {


    private static final String EXTRA_TASK_ID = "extraTaskId";
    private static final String TAG = ToDoListActivity.class.getSimpleName();
    UUID taskId;

    public static Intent newIntent(Context context, UUID uuid){
        Intent i = new Intent(context, ToDoListActivity.class);
        i.putExtra(EXTRA_TASK_ID, uuid);
        return i;
    }

    @Override
    protected Fragment createFragment() {
        taskId = (UUID) getIntent().getSerializableExtra(EXTRA_TASK_ID);
        if(taskId != null){
            return ToDoListFragment.newInstance(taskId);
        }
        return ToDoListFragment.newInstance();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        taskId = (UUID) intent.getSerializableExtra(EXTRA_TASK_ID);
        Fragment fragment;
        if(taskId != null){
            fragment = ToDoListFragment.newInstance(taskId);
        }else{
            fragment =  ToDoListFragment.newInstance();
        }
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fragment_container,fragment).commit();
    }
}
