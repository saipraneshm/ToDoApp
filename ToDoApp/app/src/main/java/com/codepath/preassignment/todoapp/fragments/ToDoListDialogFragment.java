package com.codepath.preassignment.todoapp.fragments;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

import com.codepath.preassignment.todoapp.R;
import com.codepath.preassignment.todoapp.database.ToDoListDB;
import com.codepath.preassignment.todoapp.database.ToDoListItem;
import com.codepath.preassignment.todoapp.databinding.DialogTodoItemBinding;

import java.util.UUID;


/**
 * Created by saip92 on 8/8/2017.
 */

public class ToDoListDialogFragment extends DialogFragment {

    private static final String ARGS_ITEM_ID = "itemId";
    public static final String ARGS_ADAPTER_POSITION = "adapterPosition";
    private ToDoListDB mToDoListDB;
    private int mAdapterPosition = -1;
    private UUID mId;
    private boolean isNewNote = false;


    public static ToDoListDialogFragment newInstance(UUID id, int adapterPosition){
        Bundle args = new Bundle();
        args.putSerializable(ARGS_ITEM_ID,id);
        args.putInt(ARGS_ADAPTER_POSITION, adapterPosition);

        ToDoListDialogFragment dialogFragment = new ToDoListDialogFragment();
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    public static ToDoListDialogFragment newInstance(){
        return new ToDoListDialogFragment();
    }


    @Nullable
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        if(getArguments() != null){
            mId = (UUID) getArguments().getSerializable(ARGS_ITEM_ID);
            mAdapterPosition = getArguments().getInt(ARGS_ADAPTER_POSITION);
        }
        else
            isNewNote = true;

        mToDoListDB = ToDoListDB.get(getActivity());

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final DialogTodoItemBinding binding = DataBindingUtil
                .inflate(inflater, R.layout.dialog_todo_item, null, false);

        if(isNewNote){
            binding.dialogItemToolbar.setTitle(R.string.add_new_item);
        }else{
            binding.dialogItemToolbar.setTitle(R.string.edit_existing_item);
            ToDoListItem item = mToDoListDB.getItem(mId);
            if(item != null){
                binding.dialogTitleEt.setText(item.getTitle());
                binding.dialogBodyEt.setText(item.getBody());
            }
        }


        return new AlertDialog.Builder(getActivity())
                .setView(binding.getRoot())
                .setPositiveButton(R.string.save_note, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(isNewNote)
                            addNewNote(binding.dialogTitleEt.getText().toString(),
                                binding.dialogBodyEt.getText().toString(),
                                null);
                        else
                            editNote(binding.dialogTitleEt.getText().toString(),
                                    binding.dialogBodyEt.getText().toString(),
                                    null);
                    }
                })
                .setNegativeButton(android.R.string.cancel,null)
                .create();

    }

    private void sendResult(int resultCode, int adapterPosition){
        if(getTargetFragment() == null){ return;}

        Intent intent = new Intent();
        intent.putExtra(ARGS_ADAPTER_POSITION, adapterPosition);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    private void addNewNote(String title, String body, String assignTo){
        ToDoListItem item = new ToDoListItem();
        item.setTitle(title);
        item.setBody(body);
        item.setAssignedTo(assignTo);
        mToDoListDB.addItem(item);
        sendResult(Activity.RESULT_OK, -1);
    }

    private void editNote(String title, String body, String assignTo){
        ToDoListItem item = mToDoListDB.getItem(mId);
        if(item != null){
            item.setTitle(title);
            item.setBody(body);
            item.setAssignedTo(assignTo);
            mToDoListDB.updateItem(item);
            sendResult(Activity.RESULT_OK, mAdapterPosition);
        }
    }


}
