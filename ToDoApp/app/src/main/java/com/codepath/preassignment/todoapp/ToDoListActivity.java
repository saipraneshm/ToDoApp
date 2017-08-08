package com.codepath.preassignment.todoapp;

import android.databinding.DataBindingUtil;
import android.databinding.adapters.TextViewBindingAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.codepath.preassignment.todoapp.databinding.ActivityToDoListBinding;

public class ToDoListActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityToDoListBinding binding = DataBindingUtil
                .setContentView(this,R.layout.activity_to_do_list);

        mRecyclerView = (RecyclerView) binding.getRoot();
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,3));

    }
}
