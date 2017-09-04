package com.codepath.preassignment.todoapp.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.codepath.preassignment.todoapp.activity.abs.SingleFragmentActivity;
import com.codepath.preassignment.todoapp.fragments.ToDoListFragment;

import java.util.UUID;


public class ToDoListActivity extends SingleFragmentActivity {


    private static final String EXTRA_TASK_ID = "extraTaskId";

    public static Intent newIntent(Context context, UUID uuid){
        Intent i = new Intent(context, ToDoListActivity.class);
        i.putExtra(EXTRA_TASK_ID, uuid);
        return i;
    }

    @Override
    protected Fragment createFragment() {
        UUID taskId = (UUID) getIntent().getSerializableExtra(EXTRA_TASK_ID);
        if(taskId != null){
            return ToDoListFragment.newInstance(taskId);
        }
        return ToDoListFragment.newInstance();
    }
}
