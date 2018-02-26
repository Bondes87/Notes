package com.dbondarenko.shpp.notes.ui.widgets;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import com.dbondarenko.shpp.notes.ui.adapters.NoteAdapter;
import com.dbondarenko.shpp.notes.ui.listeners.RecyclerItemTouchHelperListener;

/**
 * File: RecyclerItemTouchHelper.java
 *
 * @author Dmytro Bondarenko
 *         Date: 25.02.2018
 *         Time: 17:36
 *         E-mail: bondes87@gmail.com
 */
public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {
    private static final String TAG = RecyclerItemTouchHelper.class.getSimpleName();

    private RecyclerItemTouchHelperListener recyclerItemTouchListener;

    public RecyclerItemTouchHelper(int dragDirs, int swipeDirs,
                                   RecyclerItemTouchHelperListener recyclerItemTouchListener) {
        super(dragDirs, swipeDirs);
        this.recyclerItemTouchListener = recyclerItemTouchListener;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        Log.d(TAG, "onMove()");
        return true;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        Log.d(TAG, "onSelectedChanged()");
        if (viewHolder != null) {
            final View foregroundView =
                    ((NoteAdapter.NoteHolder) viewHolder).constraintLayoutForeground;
            getDefaultUIUtil().onSelected(foregroundView);
        }
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView,
                                RecyclerView.ViewHolder viewHolder,
                                float dX, float dY, int actionState,
                                boolean isCurrentlyActive) {
        Log.d(TAG, "onChildDrawOver()");
        final View foregroundView =
                ((NoteAdapter.NoteHolder) viewHolder).constraintLayoutForeground;
        getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView,
                dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        Log.d(TAG, "clearView()");
        final View foregroundView =
                ((NoteAdapter.NoteHolder) viewHolder).constraintLayoutForeground;
        getDefaultUIUtil().clearView(foregroundView);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState,
                            boolean isCurrentlyActive) {
        Log.d(TAG, "onChildDraw()");
        final View foregroundView =
                ((NoteAdapter.NoteHolder) viewHolder).constraintLayoutForeground;
        getDefaultUIUtil().onDraw(c, recyclerView, foregroundView,
                dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        Log.d(TAG, "onSwiped()");
        recyclerItemTouchListener.onSwiped(viewHolder, direction,
                viewHolder.getAdapterPosition());
    }
}