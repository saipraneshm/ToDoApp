package com.codepath.preassignment.todoapp.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.preassignment.todoapp.R;
import com.codepath.preassignment.todoapp.database.ToDoListDB;
import com.codepath.preassignment.todoapp.database.ToDoListItem;
import com.codepath.preassignment.todoapp.databinding.TodoItemLayoutBinding;
import com.codepath.preassignment.todoapp.viewmodel.ToDoListItemViewModel;

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
        TodoItemLayoutBinding binding = DataBindingUtil
                .inflate(inflater, R.layout.todo_item_layout, parent, false);
        return new ToDoListItemViewHolder(binding);
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
            mToDoListItems = items;
            if(mToDoListItems.size() > 0){
                notifyDataSetChanged();
            }
        }
    }

    class ToDoListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        AppCompatTextView mTextView;
        ToDoListItem mItem;
        TodoItemLayoutBinding mBinding;

        public ToDoListItemViewHolder(TodoItemLayoutBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            mTextView = mBinding.itemTitleTv;
            itemView.setOnClickListener(this);
            mBinding.setViewModel(new ToDoListItemViewModel(ToDoListDB.get(mContext)));
        }

        void bindItem(ToDoListItem item){
            if(item != null){
                mBinding.getViewModel().setToDoListItem(item);
                mBinding.executePendingBindings();
            }
        }

        @Override
        public void onClick(View view) {
            mOnItemClickListener.onItemSelected(mItem.getId(),getAdapterPosition());
        }
    }


}
