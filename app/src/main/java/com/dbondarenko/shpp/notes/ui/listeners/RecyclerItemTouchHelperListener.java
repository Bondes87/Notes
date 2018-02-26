package com.dbondarenko.shpp.notes.ui.listeners;
import android.support.v7.widget.RecyclerView;

/**
 * File: RecyclerItemTouchHelperListener.java
 *
 * @author Dmytro Bondarenko
 *         Date: 25.02.2018
 *         Time: 18:07
 *         E-mail: bondes87@gmail.com
 */
public interface RecyclerItemTouchHelperListener {
    void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
}