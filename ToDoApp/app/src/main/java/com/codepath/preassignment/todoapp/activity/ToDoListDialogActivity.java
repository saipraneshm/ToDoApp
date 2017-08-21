package com.codepath.preassignment.todoapp.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.codepath.preassignment.todoapp.activity.abs.SingleFragmentActivity;

import java.util.UUID;

public class ToDoListDialogActivity extends SingleFragmentActivity {

    private static final String ARGS_UUID = "com.codepath.preassignment.todoapp.activity.ARGS_UUID";

    public static Intent newIntent(Context context, UUID uuid){
        Intent i = new Intent(context, ToDoListDialogActivity.class);
        i.putExtra(ARGS_UUID, uuid);
        return i;
    }

    @Override
    protected Fragment createFragment() {
        UUID uuid = (UUID) getIntent().getSerializableExtra(ARGS_UUID);
        return null;
        /*if(uuid != null){
            return ToDoListFullScreenDialogFragment.newInstance(uuid);
        }else{
            return ToDoListFullScreenDialogFragment.newInstance();
        }*/
    }
}
