package com.dbondarenko.shpp.notes.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dbondarenko.shpp.notes.R;
import com.dbondarenko.shpp.notes.ui.fragments.base.BaseFragment;

public class NotesListFragment extends BaseFragment {

    public NotesListFragment() {
    }

    @Override
    public View getContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        Log.d(TAG, "getContentView");
        return inflater.inflate(R.layout.fragment_notes_list, container, false);
    }
}