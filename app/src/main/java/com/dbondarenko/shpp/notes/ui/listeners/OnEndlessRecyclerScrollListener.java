package com.dbondarenko.shpp.notes.ui.listeners;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.dbondarenko.shpp.notes.Constants;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

/**
 * File: OnEndlessRecyclerScrollListener.java
 *
 * @author Dmytro Bondarenko
 *         Date: 24.02.2018
 *         Time: 21:52
 *         E-mail: bondes87@gmail.com
 */
public abstract class OnEndlessRecyclerScrollListener extends RecyclerView.OnScrollListener {

    private static final String TAG = OnEndlessRecyclerScrollListener.class.getSimpleName();

    private int totalItemCountAfterLastLoad;
    private boolean isWaitForLoading = true;

    public abstract void onLoadMore();

    public abstract void showFloatingActionButtonAddNote();

    public abstract void hideFloatingActionButtonAddNote();

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        Log.d(TAG, "onScrolled()");

        int visibleItemCount = recyclerView.getLayoutManager().getChildCount();
        int totalItemCount = recyclerView.getLayoutManager().getItemCount();
        int firstVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager())
                .findFirstVisibleItemPosition();

        if (isWaitForLoading && totalItemCount > totalItemCountAfterLastLoad + 1) {
            isWaitForLoading = false;
            totalItemCountAfterLastLoad = totalItemCount;
        }

        if (!isWaitForLoading &&
                (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + Constants.VISIBLE_THRESHOLD)) {
            onLoadMore();
            isWaitForLoading = true;
        }
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (newState != SCROLL_STATE_IDLE) {
            hideFloatingActionButtonAddNote();
        } else {
            showFloatingActionButtonAddNote();
        }
        super.onScrollStateChanged(recyclerView, newState);
    }
}
