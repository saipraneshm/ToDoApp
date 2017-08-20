package com.codepath.preassignment.todoapp.fragments;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.preassignment.todoapp.R;
import com.codepath.preassignment.todoapp.adapter.ToDoListRecyclerViewAdapter;
import com.codepath.preassignment.todoapp.database.ToDoListDB;
import com.codepath.preassignment.todoapp.database.ToDoListItem;
import com.codepath.preassignment.todoapp.fragments.ToDoListFullScreenDialogFragment.DialogAction;

import java.util.UUID;



/**
 * Created by saip92 on 8/8/2017.
 */

public class ToDoListFragment extends Fragment {


    private static final String CREATE_NEW_TODO_DIALOG = "createNewTodoDialog";
    private static final String EDIT_TODO_DIALOG = "editTodoDialog";
    private static final int CREATE_NEW_ITEM = 0;
    private static final int EDIT_ITEM = 1;
    private static final String OPEN_TODO_FULLSCREEN_DIALOG = "openFullScreenDialog";
    private RecyclerView mRecyclerView;
    private FloatingActionButton mFAB;
    private ToDoListRecyclerViewAdapter mAdapter;
    private ToDoListDB mDB;
    private static final String TAG = ToDoListFragment.class.getSimpleName();
    private UUID mSelectedItem ;
    private Paint p = new Paint();

    public static ToDoListFragment newInstance(){
        return new ToDoListFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDB =  ToDoListDB.get(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_to_do_list, container, false);
        ((AppCompatActivity)getActivity()).setSupportActionBar((Toolbar) view.findViewById(R.id.toolbar));
        mRecyclerView = (RecyclerView) view.findViewById(R.id.todo_list_rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new ToDoListRecyclerViewAdapter(getActivity(), mDB.getAllItems());
        mAdapter.setOnItemClickListener(new ToDoListRecyclerViewAdapter.onItemClickListener() {
            @Override
            public void onItemSelected(UUID id, int position) {
                mSelectedItem = id;
                openDialog(false);
            }

            @Override
            public void onItemLongPressed(int position) {
                showDeleteAlertDialog(position);
            }


        });
        mRecyclerView.setAdapter(mAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT){

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                showDeleteAlertDialog(viewHolder.getAdapterPosition());
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {


                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    //Referred from learn2Crack.com
                    Bitmap icon = BitmapFactory.decodeResource(getResources(),
                            R.drawable.ic_delete_sweep_white_24dp);
                    p.setColor(Color.parseColor("#D32F2F"));

                    if(dX > 0){
                        RectF background = new RectF((float) itemView.getLeft(),
                                (float) itemView.getTop(),
                                dX,
                                (float) itemView.getBottom());
                        c.drawRect(background,p);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,
                                (float) itemView.getTop() + width,
                                (float) itemView.getLeft()+ 2*width,
                                (float) itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    } else {
                        RectF background = new RectF((float) itemView.getRight() + dX,
                                (float) itemView.getTop(),
                                (float) itemView.getRight(),
                                (float) itemView.getBottom());
                        c.drawRect(background,p);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,
                                (float) itemView.getTop() + width,
                                (float) itemView.getRight() - width,
                                (float) itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }).attachToRecyclerView(mRecyclerView);
        mFAB = (FloatingActionButton) view.findViewById(R.id.add_item_fab);
        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog(true);
            }
        });

        return view;
    }


    void openDialog(boolean isNewItem){
        ToDoListItem listItem;
        if(isNewItem){
            listItem = new ToDoListItem();
        }else{
            listItem = mDB.getItem(mSelectedItem);
            Log.d(TAG, "Selected Item title: " + listItem);
        }
        ToDoListFullScreenDialogFragment dialogFragment = ToDoListFullScreenDialogFragment
                .newInstance(listItem, isNewItem);
        dialogFragment.setChangeListener(new ToDoListFullScreenDialogFragment.OnDialogChangeListener() {
            @Override
            public void onDialogClose(ToDoListItem modifiedItem,
                                      DialogAction action) {
                switch(action){
                    case ADD:
                        Log.d(TAG, "calling addItem");
                        mDB.addItem(modifiedItem);
                        break;
                    case EDIT:
                        Log.d(TAG, "calling editItem");
                        mDB.updateItem(modifiedItem);
                        break;
                    case DELETE:
                        Log.d(TAG, "calling deleteItem");
                        mDB.deleteItem(modifiedItem.getId());
                        break;
                }
                mAdapter.updateItems(mDB.getAllItems());
            }
        });
        dialogFragment.show(getFragmentManager(), OPEN_TODO_FULLSCREEN_DIALOG);
    }

    public void showDeleteAlertDialog(final int position){
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.delete_dialog_title)
                .setMessage(R.string.delete_dialog_message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mDB.deleteItem(mAdapter.onItemDeleted(position));
                    }
                }).setNegativeButton(android.R.string.no, null).create();
        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }

}
