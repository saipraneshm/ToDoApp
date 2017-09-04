package com.codepath.preassignment.todoapp.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.codepath.preassignment.todoapp.R;
import com.codepath.preassignment.todoapp.adapter.ToDoListRecyclerViewAdapter;
import com.codepath.preassignment.todoapp.database.ToDoListDB;
import com.codepath.preassignment.todoapp.database.ToDoListItem;
import com.codepath.preassignment.todoapp.fragments.dialogs.ToDoListFullScreenDialogFragment;
import com.codepath.preassignment.todoapp.fragments.dialogs.ToDoListFullScreenDialogFragment.DialogAction;
import com.codepath.preassignment.todoapp.service.TaskReminderService;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;



/**
 * Created by saip92 on 8/8/2017.
 */

public class ToDoListFragment extends Fragment {


    private static final String OPEN_TODO_FULLSCREEN_DIALOG = "openFullScreenDialog";
    private static final int REQUEST_TO_OPEN_DIALOG = 54;
    private RecyclerView mRecyclerView;
    private LinearLayout mLinearLayout;
    private AppCompatButton mAddItemButton;
    private FloatingActionButton mFAB;
    private ToDoListRecyclerViewAdapter mAdapter;
    private ToDoListDB mDB;
    private CoordinatorLayout mCoordinatorLayout;
    private static final String TAG = ToDoListFragment.class.getSimpleName();
    private UUID mSelectedItem ;
    private Paint p = new Paint();
    boolean itemDeleted = false;
    private static final String EXTRA_TASK_ID = "ToDoListFragment.extraTaskID";

    public static ToDoListFragment newInstance(){
        return new ToDoListFragment();
    }

    public static ToDoListFragment newInstance(UUID uuid){
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_TASK_ID,uuid);

        ToDoListFragment fragment = new ToDoListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDB =  ToDoListDB.get(getActivity());
        if(getArguments() != null){
            mSelectedItem = (UUID) getArguments().getSerializable(EXTRA_TASK_ID);
            if(mSelectedItem != null){
                openDialog(false);
            }
        }
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_to_do_list, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.todo_list_rv);
        mLinearLayout = (LinearLayout) view.findViewById(R.id.display_add_item_ll);
        mAddItemButton = (AppCompatButton) view.findViewById(R.id.add_item_button);
        mCoordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinator_layout_todo_list);

        mAddItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog(true);
            }
        });

        updateUI();

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
        dialogFragment.setTargetFragment(this, REQUEST_TO_OPEN_DIALOG);
        dialogFragment.show(getFragmentManager(), OPEN_TODO_FULLSCREEN_DIALOG);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK) return;

        if(requestCode ==  REQUEST_TO_OPEN_DIALOG){
            DialogAction action = (DialogAction) data
                    .getSerializableExtra(ToDoListFullScreenDialogFragment.EXTRA_ACTION);
            ToDoListItem modifiedItem = data
                    .getParcelableExtra(ToDoListFullScreenDialogFragment.EXTRA_ITEM);
            switch(action){
                case ADD:
                    Log.d(TAG, "calling addItem");
                    setTaskTimer(modifiedItem);
                    mDB.addItem(modifiedItem);
                    showSnackBar(getString(R.string.added_new_item));
                    break;
                case EDIT:
                    Log.d(TAG, "calling editItem");
                    TaskReminderService.cancelTaskReminder(getActivity(),modifiedItem);
                    setTaskTimer(modifiedItem);
                    mDB.updateItem(modifiedItem);
                    showSnackBar(getString(R.string.modified_existing_item));
                    break;
                case DELETE:
                    Log.d(TAG, "calling deleteItem");
                    mAdapter.deleteItem(modifiedItem.getId());
                    TaskReminderService.cancelTaskReminder(getActivity(),modifiedItem);
                    mDB.deleteItem(modifiedItem.getId());
                    showSnackBar(getString(R.string.deleted_item));
                    break;
            }
            mAdapter.updateItems(mDB.getAllItems());
            updateUI();
        }

    }

    private void setTaskTimer(ToDoListItem item){
        int currentTimeInMillis;
        if(item.getDateCreated() == null){
            currentTimeInMillis = (int) System.currentTimeMillis();
            item.setDateCreated(currentTimeInMillis + "");
        }
        currentTimeInMillis = Integer.parseInt(item.getDateCreated());
        item.setDateCreated(currentTimeInMillis + "");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(item.getDueDate());
        if(calendar.getTimeInMillis() > System.currentTimeMillis())
            TaskReminderService.setTaskReminder(getActivity(),item);
    }



    private void showSnackBar(String message){
        final Snackbar snackbar = Snackbar.make(mCoordinatorLayout,
                message,
                Snackbar.LENGTH_SHORT);
        snackbar.setActionTextColor(ContextCompat.getColor(getActivity(),R.color.colorPrimary))
                .setAction(R.string.dismiss, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                }).show();
    }

    public void showDeleteAlertDialog(final int position){

        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.delete_dialog_title)
                .setMessage(R.string.delete_dialog_message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        itemDeleted = true;
                        mDB.deleteItem(mAdapter.onItemDeleted(position));
                        updateUI();
                        showSnackBar(getString(R.string.deleted_item));
                    }
                }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        itemDeleted= false;
                    }
                }).create();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if(!itemDeleted){
                    Log.d(TAG, "calling notify item changed");
                    mAdapter.notifyItemChanged(position);
                }
            }
        });
        dialog.show();
    }

    private void updateUI(){
        int itemCount = mDB.getAllItems().size();
        if(itemCount > 0){
            mLinearLayout.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
        if(itemCount == 0){
            mRecyclerView.setVisibility(View.GONE);
            mLinearLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
       /* mAdapter.notifyDataSetChanged();*/
    }

}
