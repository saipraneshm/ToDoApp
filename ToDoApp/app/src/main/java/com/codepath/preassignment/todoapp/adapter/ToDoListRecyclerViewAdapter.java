package com.codepath.preassignment.todoapp.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.preassignment.todoapp.R;
import com.codepath.preassignment.todoapp.database.ToDoListDB;
import com.codepath.preassignment.todoapp.database.ToDoListItem;
import com.codepath.preassignment.todoapp.utils.MultiChoiceHelper;
import com.codepath.preassignment.todoapp.utils.Priority;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by saip92 on 8/8/2017.
 */

public class ToDoListRecyclerViewAdapter extends
        RecyclerView.Adapter<ToDoListRecyclerViewAdapter.ToDoListItemViewHolder> {

    private Context mContext;
    private List<ToDoListItem> mToDoListItems;
    private onItemClickListener mOnItemClickListener;
    private MultiChoiceHelper mMultiChoiceHelper;
    private boolean isActive = true;
    private boolean isClickable = true;


    private static final String TAG = ToDoListRecyclerViewAdapter.class.getSimpleName();
    public interface onItemClickListener{
        void onItemSelected(UUID id, int position);
        void onAllItemsDeleted();
        void onItemsMarkedDone();
        void onActionModeEnabled();
        void onActionModeDisabled();
    }

    public ToDoListRecyclerViewAdapter(Context context, List<ToDoListItem> items, boolean active){
        isActive = active;
        mContext = context;
        mToDoListItems = items;
        mMultiChoiceHelper = new MultiChoiceHelper((AppCompatActivity) context, this);
        mMultiChoiceHelper
                .setMultiChoiceModeListener(new MultiChoiceHelper.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id,
                                                  boolean checked) {
                updateSelectedCountDisplay(mode);
            }

            private void updateSelectedCountDisplay(ActionMode mode) {
                int count = mMultiChoiceHelper.getCheckedItemCount();
                mode.setTitle(mMultiChoiceHelper.getContext().getResources()
                        .getQuantityString(R.plurals.selected, count, count));
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.action_dialog_menu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                if(!isActive) menu.getItem(0).setVisible(false);
                updateSelectedCountDisplay(mode);
                mOnItemClickListener.onActionModeEnabled();
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                SparseBooleanArray array = mMultiChoiceHelper.getCheckedItemPositions();
                int checkedItemCount = mMultiChoiceHelper.getCheckedItemCount();
                final int start = array.keyAt(0);
                final int end = array.keyAt(array.size() - 1);

                Log.d(TAG, "array size: " + array.size() + ", start " + start
                        + ", end " + end + ", checkedItemCount " + checkedItemCount);

                HashMap<UUID, ToDoListItem> currentList = new HashMap<>();

                for(int i = 0 ; i < checkedItemCount; i++){
                    int position = array.keyAt(i);
                  //  Log.d(TAG,"Adapter position: " + position);
                    UUID currentID = getUUID(position);
                    currentList.put(currentID, mToDoListItems.get(position));
                }
                switch (item.getItemId()) {
                    case R.id.action_delete_items:
                        for(UUID id : currentList.keySet()){
                            ToDoListDB db= ToDoListDB.get(mContext);
                            if(id != null){
                                deleteItem(id);
                                db.deleteItem(id);
                            }
                        }

                        if(mToDoListItems.size() == 0){
                            mOnItemClickListener.onAllItemsDeleted();
                        }
                        mode.finish();
                        return true;
                    case R.id.action_done:
                        ToDoListItem toDoListItem;
                        for(UUID id : currentList.keySet()){
                            ToDoListDB db = ToDoListDB.get(mContext);

                            if(id != null){
                                deleteItem(id);
                                toDoListItem = currentList.get(id);
                                toDoListItem.setTaskStatus(true);
                                toDoListItem.setLastUpdated(SystemClock.currentThreadTimeMillis()
                                        + "");
                                db.updateItem(toDoListItem);
                            }
                        }
                        if(checkedItemCount > 0){
                            mOnItemClickListener.onItemsMarkedDone();
                        }

                        mode.finish();
                        return true;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                notifyDataSetChanged();
                mOnItemClickListener.onActionModeDisabled();
            }
        });
    }

    public void setOnItemClickListener(onItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setClickable(boolean val){
        isClickable = val;
    }

    public Parcelable onSaveInstanceState() {
        return mMultiChoiceHelper.onSaveInstanceState();
    }

    public void onRestoreInstanceState(Parcelable state) {
        mMultiChoiceHelper.onRestoreInstanceState(state);
    }

    public void onDestroyView() {
        mMultiChoiceHelper.clearChoices();
    }

    @Override
    public ToDoListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.todo_item_layout, parent, false);
        return new ToDoListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ToDoListItemViewHolder holder, int position) {
        holder.bindItem(mToDoListItems.get(position));
        holder.bind(mMultiChoiceHelper,position);
    }

    @Override
    public int getItemCount() {
        return mToDoListItems.size();
    }

    public void updateItems(List<ToDoListItem> items){
        if(items != null){
            mToDoListItems.clear();
            mToDoListItems.addAll(items);
            if(mToDoListItems.size() > 0){
                notifyDataSetChanged();
            }
        }
    }

    public UUID onItemDeleted(int position){
        if(mToDoListItems.size() > 0){
            UUID tempId = mToDoListItems.get(position).getId();
            mToDoListItems.remove(position);
            notifyItemRemoved(position);
            return tempId;
        }
        return null;
    }

    private UUID getUUID(int position){
        if(mToDoListItems.size()  > 0 && position < mToDoListItems.size()){
            return mToDoListItems.get(position).getId();
        }
        return null;
    }

    public void deleteItem(UUID mId){
        int pos = getItemPosition(mId);
        if(pos >= 0){
            mToDoListItems.remove(pos);
            notifyItemRemoved(pos);
        }
    }

    private int getItemPosition(UUID mId){
        for(int i = 0 ; i < mToDoListItems.size(); i++){
            if(mId.equals(mToDoListItems.get(i).getId())){
                return i;
            }
        }
        return -1;
    }


    public class ToDoListItemViewHolder extends MultiChoiceHelper.ViewHolder implements
            View.OnClickListener, View.OnLongClickListener {

        AppCompatTextView mTitleTv, mPriorityTv, mDueDateTv, mDueTimeTv;
        ToDoListItem mItem;

        public ToDoListItemViewHolder(View view) {
            super(view);
            mTitleTv = (AppCompatTextView) view.findViewById(R.id.item_title_tv);
            mPriorityTv = (AppCompatTextView) view.findViewById(R.id.priority_tv);
            mDueDateTv = (AppCompatTextView) view.findViewById(R.id.date_tv);
            mDueTimeTv = (AppCompatTextView) view.findViewById(R.id.time_tv);
            if(isClickable)
                setOnClickListener(this);
            else
                setOnClickListener(null);
        }

        void bindItem(ToDoListItem item){
            if(item != null){
                if(item.isTaskDone()) itemView.setAlpha(0.5f);
                mItem = item;
                mTitleTv.setText(item.getTitle());
                setPriorityTv(item.getPriority());
                if(mItem.hasReminder())
                    updateDate(item.getDueDate());
            }
        }

        private void updateTime(Date date) {
            DateFormat dateFormat = SimpleDateFormat.getTimeInstance(DateFormat.SHORT, Locale.US);
            String strDate = dateFormat.format(date);
            mDueDateTv.setText(strDate);
        }

        private void updateDate(Date date) {
            DateFormat dateFormat = SimpleDateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);
            String strDate = dateFormat.format(date);
            mDueTimeTv.setText(strDate);
            updateTime(date);
        }

        private void setPriorityTv(int priority){
            int magnitudeColor;
            String priorityChar;
            switch (priority){
                case Priority.HIGH: magnitudeColor = R.color.colorHighPriority;
                    priorityChar = "H";
                    break;
                case Priority.LOW: magnitudeColor = R.color.colorLowPriority;
                    priorityChar = "L";
                    break;
                case Priority.MEDIUM: magnitudeColor = R.color.colorMediumPriority;
                    priorityChar = "M";
                    break;
                default:
                    magnitudeColor = R.color.colorLowPriority;
                    priorityChar = "L";
            }
            GradientDrawable priorityCircle = (GradientDrawable)mPriorityTv.getBackground();
            priorityCircle.setColor(ContextCompat.getColor(mContext, magnitudeColor));
            mPriorityTv.setText(priorityChar);
        }

        @Override
        public void onClick(View view) {
            if(mItem != null)
                mOnItemClickListener.onItemSelected(mItem.getId(),getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    }


}
