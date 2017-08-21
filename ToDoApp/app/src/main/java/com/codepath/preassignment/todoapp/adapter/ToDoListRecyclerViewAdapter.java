package com.codepath.preassignment.todoapp.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.preassignment.todoapp.R;
import com.codepath.preassignment.todoapp.database.ToDoListDB;
import com.codepath.preassignment.todoapp.database.ToDoListItem;


import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

/**
 * Created by saip92 on 8/8/2017.
 */

public class ToDoListRecyclerViewAdapter extends
        RecyclerView.Adapter<ToDoListRecyclerViewAdapter.ToDoListItemViewHolder> {

    Context mContext;
    List<ToDoListItem> mToDoListItems;
    onItemClickListener mOnItemClickListener;


    public interface onItemClickListener{
        void onItemSelected(UUID id, int position);
        void onItemLongPressed(int position);
    }

    public ToDoListRecyclerViewAdapter(Context context, List<ToDoListItem> items){
        mContext = context;
        mToDoListItems = items;
    }

    public void setOnItemClickListener(onItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
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
                //Log.d("CHECK", "calling notifydatasetchanged");
                for(ToDoListItem item : items){
                    Log.d("CHECK", item.getTitle()  + " <- checking this item");
                }
                notifyDataSetChanged();
            }
        }
    }

    public UUID onItemDeleted(int position){
        UUID tempId = mToDoListItems.get(position).getId();
        mToDoListItems.remove(position);
        notifyItemRemoved(position);
        return tempId;
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

    public class ToDoListItemViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener, View.OnLongClickListener {

        AppCompatTextView mTextView;
        ToDoListItem mItem;

        public ToDoListItemViewHolder(View binding) {
            super(binding);
            mTextView = (AppCompatTextView) binding.findViewById(R.id.item_title_tv);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        void bindItem(ToDoListItem item){
            if(item != null){
                mItem = item;
                mTextView.setText(item.getTitle());
            }
        }

        @Override
        public void onClick(View view) {
            if(mItem != null)
                mOnItemClickListener.onItemSelected(mItem.getId(),getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            if(mItem != null){
                mOnItemClickListener.onItemLongPressed(getAdapterPosition());
            }
            return false;
        }
    }


}
