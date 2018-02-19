package com.dbondarenko.shpp.notes.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dbondarenko.shpp.notes.R;
import com.dbondarenko.shpp.notes.ui.fragments.base.BaseFragment;

public class NoteFragment extends BaseFragment {

    public NoteFragment() {
    }

    @Override
    public View getContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        Log.d(TAG, "getContentView");
        return inflater.inflate(R.layout.fragment_note, container, false);
    }
}