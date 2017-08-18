package com.codepath.preassignment.todoapp.fragments;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.preassignment.todoapp.R;
import com.codepath.preassignment.todoapp.adapter.ToDoListRecyclerViewAdapter;
import com.codepath.preassignment.todoapp.database.ToDoListDB;
import com.codepath.preassignment.todoapp.database.ToDoListItem;
import com.codepath.preassignment.todoapp.databinding.FragmentToDoListBinding;

import java.util.UUID;


/**
 * Created by saip92 on 8/8/2017.
 */

public class ToDoListFragment extends Fragment {


    private static final String CREATE_NEW_TODO_DIALOG = "createNewTodoDialog";
    private static final String EDIT_TODO_DIALOG = "editTodoDialog";
    private static final int CREATE_NEW_ITEM = 0;
    private static final int EDIT_ITEM = 1;
    private RecyclerView mRecyclerView;
    private FloatingActionButton mFAB;
    private ToDoListRecyclerViewAdapter mAdapter;
    private ToDoListDB mDB;
    private static final String TAG = ToDoListFragment.class.getSimpleName();

    public static ToDoListFragment newInstance(){
        return new ToDoListFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDB =  ToDoListDB.get(getActivity());

        for(ToDoListItem item : mDB.getAllItems()){
            Log.d(TAG, item.getTitle());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        FragmentToDoListBinding binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_to_do_list, container, false);

        ((AppCompatActivity)getActivity()).setSupportActionBar(binding.toolbar);

        mRecyclerView = binding.todoListRv;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        mAdapter = new ToDoListRecyclerViewAdapter(getActivity(), mDB.getAllItems());
        mAdapter.setOnItemClickListener(new ToDoListRecyclerViewAdapter.onItemClickListener() {
            @Override
            public void onItemSelected(UUID id, int position) {
                editItem(id,position);
            }

        });
        mRecyclerView.setAdapter(mAdapter);

        mFAB = binding.addItemFab;

        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem();
            }
        });

        return binding.getRoot();
    }

    void addItem(){
        ToDoListDialogFragment dialogFragment = ToDoListDialogFragment.newInstance();
        dialogFragment.setTargetFragment(this, CREATE_NEW_ITEM);
        dialogFragment.show(getFragmentManager(), CREATE_NEW_TODO_DIALOG);
    }

    void editItem(UUID id, int adapterPos){
        ToDoListDialogFragment dialogFragment = ToDoListDialogFragment.newInstance(id, adapterPos);
        dialogFragment.setTargetFragment(this, EDIT_ITEM);
        dialogFragment.show(getFragmentManager(), EDIT_TODO_DIALOG);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK) return;

        if(requestCode == CREATE_NEW_ITEM){
            mAdapter.updateItems(mDB.getAllItems());
        }

        if(requestCode == EDIT_ITEM){
            int adapterPosition = data.getIntExtra(ToDoListDialogFragment.ARGS_ADAPTER_POSITION, -1);
            if(adapterPosition >= 0){
                Log.d(TAG, " calling notify item changed: " + adapterPosition);
                //mAdapter.notifyItemChanged(adapterPosition);
                mAdapter.updateItems(mDB.getAllItems());
            }
        }
    }
}
