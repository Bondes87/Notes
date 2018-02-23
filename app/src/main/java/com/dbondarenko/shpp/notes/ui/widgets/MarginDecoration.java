package com.dbondarenko.shpp.notes.ui.widgets;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.dbondarenko.shpp.notes.R;

/**
 * File: MarginDecoration.java
 *
 * @author Dmytro Bondarenko
 *         Date: 23.02.2018
 *         Time: 11:51
 *         E-mail: bondes87@gmail.com
 */
public class MarginDecoration extends RecyclerView.ItemDecoration {
    private static final String TAG = MarginDecoration.class.getSimpleName();

    private int marginSmall;
    private int margin;

    public MarginDecoration(Context context) {
        Log.d(TAG, "MarginDecoration()");
        margin = context.getResources().getDimensionPixelOffset(R.dimen.small_margin_between_content);
        marginSmall = margin / 2;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        Log.d(TAG, "getItemOffsets()");
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.set(margin, margin, margin, marginSmall);
        } else if (parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() - 1) {
            outRect.set(margin, marginSmall, margin, margin);
        } else {
            outRect.set(margin, marginSmall, margin, marginSmall);
        }
    }
}