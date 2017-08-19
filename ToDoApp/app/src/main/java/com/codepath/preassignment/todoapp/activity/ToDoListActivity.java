package com.codepath.preassignment.todoapp.activity;

import android.support.v4.app.Fragment;

import com.codepath.preassignment.todoapp.activity.abs.SingleFragmentActivity;
import com.codepath.preassignment.todoapp.fragments.ToDoListFragment;


public class ToDoListActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        return ToDoListFragment.newInstance();
    }
}
