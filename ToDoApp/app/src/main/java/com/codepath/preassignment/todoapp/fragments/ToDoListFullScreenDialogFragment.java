package com.codepath.preassignment.todoapp.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.preassignment.todoapp.R;
import com.codepath.preassignment.todoapp.database.ToDoListDB;
import com.codepath.preassignment.todoapp.database.ToDoListItem;

import java.util.UUID;

/**
 * Created by saip92 on 8/18/2017.
 */

public class ToDoListFullScreenDialogFragment extends DialogFragment implements View.OnClickListener {


    private Toolbar mDialogToolbar;
    private TextInputEditText mTitleEditText, mDueDateEditText, mDueTimeEditText, mPriorityEditText,
    mItemDescEditText, mItemStatusDesc;
    private ToDoListDB mToDoListDB;
    private static final String ARGS_ITEM_ID = "itemId";
    private UUID mId;
    private boolean isNewNote = false;

    MenuItem editItem;
    MenuItem saveItem;

    private Menu mMenu;


    boolean valuesChanged = false;

    public static ToDoListFullScreenDialogFragment newInstance(UUID id){
        Bundle args = new Bundle();
        args.putSerializable(ARGS_ITEM_ID,id);

        ToDoListFullScreenDialogFragment dialogFragment = new ToDoListFullScreenDialogFragment();
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    public static ToDoListFullScreenDialogFragment newInstance(){
        return new ToDoListFullScreenDialogFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mToDoListDB = ToDoListDB.get(getActivity());
        if(getArguments() != null){
            mId = (UUID) getArguments().getSerializable(ARGS_ITEM_ID);
        }else{
            isNewNote = true;
        }


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_full_screen, container, false);
        mTitleEditText = (TextInputEditText) view.findViewById(R.id.title_edit_text);
        mDueDateEditText = (TextInputEditText)view.findViewById(R.id.due_date_edit_text);
        mDueTimeEditText = (TextInputEditText)view.findViewById(R.id.time_edit_text);
        mPriorityEditText = (TextInputEditText)view.findViewById(R.id.priority_edit_text);
        mItemDescEditText = (TextInputEditText)view.findViewById(R.id.item_desc_edit_text);
        mItemStatusDesc = (TextInputEditText)view.findViewById(R.id.item_status_edit_text);

        mDialogToolbar = (Toolbar) view.findViewById(R.id.dialog_toolbar);
        mDialogToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });


        mDialogToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                int itemId = item.getItemId();

                switch (itemId){
                    case R.id.action_edit_item:
                        enableAllFields();
                        editItem.setVisible(false);
                        saveItem.setVisible(true);
                        break;
                    case R.id.action_delete_item:
                        deleteNote();
                        getActivity().finish();
                        break;
                    case R.id.action_save_item:
                        saveAllFields();
                        break;
                }
                return true;
            }
        });
        mDialogToolbar.inflateMenu(R.menu.dialog_menu);
        mMenu = mDialogToolbar.getMenu();
        editItem = mMenu.findItem(R.id.action_edit_item);
        saveItem = mMenu.findItem(R.id.action_save_item);
        if(isNewNote){
            mDialogToolbar.setTitle(R.string.add_new_item);
            editItem.setVisible(false);
            saveItem.setVisible(true);
        }else{
            disableAllFields();
            saveItem.setVisible(false);
            editItem.setVisible(true);
            mDialogToolbar.setTitle(R.string.edit_existing_item);
            updateUI(mId);
        }
        return view;
    }

    private void updateUI(UUID id) {
        if(id != null){
            ToDoListItem item = mToDoListDB.getItem(id);
            if(item != null){
                mTitleEditText.setText(item.getTitle());
                mItemDescEditText.setText(item.getBody());
            }
        }

    }

    private void enableAllFields() {

        mTitleEditText.append("");
        mTitleEditText.setFocusableInTouchMode(true);
        mTitleEditText.requestFocus();
        mTitleEditText.setClickable(true);
        mDueDateEditText.setClickable(true);
        mDueTimeEditText.setClickable(true);
        mPriorityEditText.setClickable(true);
        mItemDescEditText.setClickable(true);
        mItemStatusDesc.setClickable(true);

        mTitleEditText.setFocusableInTouchMode(true);
        mDueDateEditText.setFocusableInTouchMode(true);
        mDueTimeEditText.setFocusableInTouchMode(true);
        mPriorityEditText.setFocusableInTouchMode(true);
        mItemDescEditText.setFocusableInTouchMode(true);
        mItemStatusDesc.setFocusableInTouchMode(true);

    }

    private void disableAllFields(){
        mTitleEditText.setClickable(false);
        mDueDateEditText.setClickable(false);
        mDueTimeEditText.setClickable(false);
        mPriorityEditText.setClickable(false);
        mItemDescEditText.setClickable(false);
        mItemStatusDesc.setClickable(false);

        mTitleEditText.setFocusableInTouchMode(false);
        mDueDateEditText.setFocusableInTouchMode(false);
        mDueTimeEditText.setFocusableInTouchMode(false);
        mPriorityEditText.setFocusableInTouchMode(false);
        mItemDescEditText.setFocusableInTouchMode(false);
        mItemStatusDesc.setFocusableInTouchMode(false);
    }

    private void saveAllFields(){
        if(isNewNote){
            addNewNote();
        }else{
            editNote();
        }
        getActivity().finish();
    }

    private void sendResult(int resultCode){
       getActivity().setResult(resultCode);
    }

    private void addNewNote(){
        ToDoListItem item = new ToDoListItem();
        item.setTitle(mTitleEditText.getText().toString());
        item.setBody(mItemDescEditText.getText().toString());
        mToDoListDB.addItem(item);
        sendResult(Activity.RESULT_OK);
    }

    private void editNote(){
        ToDoListItem item = mToDoListDB.getItem(mId);
        if(item != null){
            item.setTitle(mTitleEditText.getText().toString());
            item.setBody(mItemDescEditText.getText().toString());
            mToDoListDB.updateItem(item);
            sendResult(Activity.RESULT_OK);
        }
    }

    private void deleteNote(){
        if(mId != null){
            mToDoListDB.deleteItem(mId);
        }
    }

    @Override
    public void onClick(View view) {

    }
}
