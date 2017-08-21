package com.codepath.preassignment.todoapp.fragments.dialogs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.preassignment.todoapp.helper.Priority;
import com.codepath.preassignment.todoapp.R;
import com.codepath.preassignment.todoapp.database.ToDoListDB;
import com.codepath.preassignment.todoapp.database.ToDoListItem;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by saip92 on 8/18/2017.
 */

public class ToDoListFullScreenDialogFragment extends DialogFragment implements View.OnClickListener {


    private static final String TAG = ToDoListFullScreenDialogFragment.class.getSimpleName();
    private static final String ARGS_IS_NEW_NOTE = "ISNEWNOTE";
    private static final int REQUEST_DATE = 0;
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_TIME = 1;
    private static final String DIALOG_TIME = "DialogTime";
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
        mItemStatusDesc.setOnClickListener(this);
        mDueDateEditText.setOnClickListener(this);
        mDueTimeEditText.setOnClickListener(this);

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
            enableAllFields();
            updateUI();
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
            mItemStatusDesc.setText(mToDoListItem.isTaskDone()?
                    getString(R.string.task_status_done) :
                    getString(R.string.task_status_incomplete));
            if(!isNewNote){
                updateDate(mToDoListItem.getDueDate());
                updateTime(mToDoListItem.getDueDate());
            }
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

        mItemDescEditText.setFocusableInTouchMode(true);

    }

    private void disableAllFields(){
        mTitleEditText.setOnClickListener(null);
        mDueDateEditText.setOnClickListener(null);
        mDueTimeEditText.setOnClickListener(null);
        mPriorityEditText.setOnClickListener(null);
        mItemDescEditText.setOnClickListener(null);
        mItemStatusDesc.setOnClickListener(null);

        mTitleEditText.setFocusableInTouchMode(false);
        mItemDescEditText.setFocusableInTouchMode(false);
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
                mToDoListItem.setTitle(mTitleEditText.getText().toString().trim());
                mToDoListItem.setBody(mItemDescEditText.getText().toString());
                mToDoListItem.setPriority(Priority.getInt(mPriorityEditText.getText().toString()));
                mToDoListItem.setTaskStatus(getTaskBoolVal(mItemStatusDesc.getText().toString()));
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


    private void showTaskStatusPopUp(){
        PopupMenu popup = new PopupMenu(getActivity(),mItemStatusDesc);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String action = null;
                switch (item.getItemId()){
                    case R.id.action_task_status_done:
                        action = getString(R.string.task_status_done);
                        break;
                    case R.id.action_task_status_not_done:
                        action = getString(R.string.task_status_incomplete);
                        break;

                }
                if(action != null)
                    mItemStatusDesc.setText(action);
                return true;
            }
        });
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.task_status_popup_menu, popup.getMenu());
        popup.show();
    }


    private void showDateDialogPicker(){
        FragmentManager fm = getFragmentManager();
        DatePickerDialogFragment dialogFragment = DatePickerDialogFragment
                .newInstance(mToDoListItem.getDueDate());
        dialogFragment.setTargetFragment(this, REQUEST_DATE);
        dialogFragment.show(fm, DIALOG_DATE);
    }

    private void showTimeDialogPicker(){
        FragmentManager fm = getFragmentManager();
        TimePickerDialogFragment dialogFragment = TimePickerDialogFragment
                .newInstance(mToDoListItem.getDueDate());
        dialogFragment.setTargetFragment(this, REQUEST_TIME);
        dialogFragment.show(fm, DIALOG_TIME);
    }

    private boolean getTaskBoolVal(String str){
        return str.equals(getString(R.string.task_status_done));
    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.priority_edit_text:
                showPriorityPopUp();
                break;
            case R.id.item_status_edit_text:
                showTaskStatusPopUp();
                break;
            case R.id.due_date_edit_text:
                showDateDialogPicker();
                break;
            case R.id.time_edit_text:
                showTimeDialogPicker();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK) return;
        if(requestCode == REQUEST_DATE){
            Date date = (Date) data.getSerializableExtra(DatePickerDialogFragment.EXTRA_DATE);
            updateDate(date);
        }else if(requestCode == REQUEST_TIME){
            Date date = (Date) data.getSerializableExtra(TimePickerDialogFragment.EXTRA_TIME);
            updateTime(date);
        }
    }

    private void updateTime(Date date) {
        DateFormat dateFormat = SimpleDateFormat.getTimeInstance(DateFormat.SHORT, Locale.US);
        String strDate = dateFormat.format(date);
        mDueTimeEditText.setText(strDate);
        mToDoListItem.setDueDate(date);
    }

    private void updateDate(Date date) {
        DateFormat dateFormat = SimpleDateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);
        String strDate = dateFormat.format(date);
        mDueDateEditText.setText(strDate);
        mToDoListItem.setDueDate(date);
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

