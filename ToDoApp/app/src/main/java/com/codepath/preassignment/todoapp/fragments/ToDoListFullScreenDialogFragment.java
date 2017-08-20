package com.codepath.preassignment.todoapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.preassignment.todoapp.Priority;
import com.codepath.preassignment.todoapp.R;
import com.codepath.preassignment.todoapp.database.ToDoListDB;
import com.codepath.preassignment.todoapp.database.ToDoListItem;

import java.util.UUID;

/**
 * Created by saip92 on 8/18/2017.
 */

public class ToDoListFullScreenDialogFragment extends DialogFragment implements View.OnClickListener {


    private static final String TAG = ToDoListFullScreenDialogFragment.class.getSimpleName();
    private static final String ARGS_IS_NEW_NOTE = "ISNEWNOTE";
    private Toolbar mDialogToolbar;
    private TextInputEditText mTitleEditText, mDueDateEditText, mDueTimeEditText, mPriorityEditText,
    mItemDescEditText, mItemStatusDesc;
    private ToDoListDB mToDoListDB;
    private static final String ARGS_ITEM = "itemId";
    private UUID mId;
    private boolean isNewNote = false;
    public enum DialogAction{
        ADD,
        DELETE,
        EDIT
    }

    MenuItem editItem;
    MenuItem saveItem;

    private Menu mMenu;


    boolean valuesChanged = false;
    private OnDialogChangeListener mChangeListener;
    private ToDoListItem mToDoListItem;

    public interface OnDialogChangeListener{
        void onDialogClose(ToDoListItem modifiedItem, DialogAction action);
    }

    public static ToDoListFullScreenDialogFragment newInstance(ToDoListItem listItem, boolean isNewNote){
        Bundle args = new Bundle();
        args.putParcelable(ARGS_ITEM,listItem);
        args.putBoolean(ARGS_IS_NEW_NOTE, isNewNote);

        ToDoListFullScreenDialogFragment dialogFragment = new ToDoListFullScreenDialogFragment();
        dialogFragment.setArguments(args);
        return dialogFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME,R.style.AppTheme_NoActionBar);
        mToDoListDB = ToDoListDB.get(getActivity());
        if(getArguments() != null) {
            mToDoListItem = getArguments().getParcelable(ARGS_ITEM);
            isNewNote = getArguments().getBoolean(ARGS_IS_NEW_NOTE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_full_screen, container, false);
        mTitleEditText = (TextInputEditText) view.findViewById(R.id.title_edit_text);
        mDueDateEditText = (TextInputEditText)view.findViewById(R.id.due_date_edit_text);
        mDueTimeEditText = (TextInputEditText)view.findViewById(R.id.time_edit_text);
        mPriorityEditText = (TextInputEditText)view.findViewById(R.id.priority_edit_text);
        mItemDescEditText = (TextInputEditText)view.findViewById(R.id.item_desc_edit_text);
        mItemStatusDesc = (TextInputEditText)view.findViewById(R.id.item_status_edit_text);

        mPriorityEditText.setOnClickListener(this);

        mDialogToolbar = (Toolbar) view.findViewById(R.id.dialog_toolbar);
        mDialogToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
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
                        handleAction(DialogAction.DELETE);
                        dismiss();
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
            updateUI();
        }
        return view;
    }

    private void updateUI() {
        if(mToDoListItem != null){
            mTitleEditText.setText(mToDoListItem.getTitle());
            mItemDescEditText.setText(mToDoListItem.getBody());
            mPriorityEditText.setText(Priority.getString(mToDoListItem.getPriority()));
        }
    }

    private void enableAllFields() {

        mTitleEditText.append("");
        mTitleEditText.setFocusableInTouchMode(true);
        mTitleEditText.requestFocus();

        mTitleEditText.setOnClickListener(this);
        mDueDateEditText.setOnClickListener(this);
        mDueTimeEditText.setOnClickListener(this);
        mPriorityEditText.setOnClickListener(this);
        mItemDescEditText.setOnClickListener(this);
        mItemStatusDesc.setOnClickListener(this);

        mDueDateEditText.setFocusableInTouchMode(true);
        mDueTimeEditText.setFocusableInTouchMode(true);
        mItemDescEditText.setFocusableInTouchMode(true);
        mItemStatusDesc.setFocusableInTouchMode(true);

    }

    private void disableAllFields(){
        mTitleEditText.setOnClickListener(null);
        mDueDateEditText.setOnClickListener(null);
        mDueTimeEditText.setOnClickListener(null);
        mPriorityEditText.setOnClickListener(null);
        mItemDescEditText.setOnClickListener(null);
        mItemStatusDesc.setOnClickListener(null);

        mTitleEditText.setFocusableInTouchMode(false);
        mDueDateEditText.setFocusableInTouchMode(false);
        mDueTimeEditText.setFocusableInTouchMode(false);
        mItemDescEditText.setFocusableInTouchMode(false);
        mItemStatusDesc.setFocusableInTouchMode(false);
    }

    private void saveAllFields(){
        if(isNewNote){
            handleAction(DialogAction.ADD);
        }else{
            handleAction(DialogAction.EDIT);
        }
        dismiss();
    }


    private void handleAction(DialogAction action){
        if(mToDoListItem != null){
            if(action != DialogAction.DELETE){
                mToDoListItem.setTitle(mTitleEditText.getText().toString());
                mToDoListItem.setBody(mItemDescEditText.getText().toString());
                mToDoListItem.setPriority(Priority.getInt(mPriorityEditText.getText().toString()));
            }
            mChangeListener.onDialogClose(mToDoListItem,action);
        }
    }

    private void showPriorityPopUp(){
        PopupMenu popup = new PopupMenu(getActivity(),mPriorityEditText);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String action = null;
                switch (item.getItemId()){
                    case R.id.action_high_priority:
                        action = Priority.getString(Priority.HIGH);
                        break;
                    case R.id.action_medium_priority:
                        action = Priority.getString(Priority.MEDIUM);
                        break;
                    case R.id.action_low_priority:
                        action = Priority.getString(Priority.LOW);
                        break;

                }
                if(action != null)
                    mPriorityEditText.setText(action);
                return true;
            }
        });
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.priority_popup_menu, popup.getMenu());
        popup.show();
    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.priority_edit_text:
                showPriorityPopUp();
                break;
        }
    }

    public void setChangeListener(OnDialogChangeListener changeListener) {
        mChangeListener = changeListener;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mChangeListener = null;
    }
}
