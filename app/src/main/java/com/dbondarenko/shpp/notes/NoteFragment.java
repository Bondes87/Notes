package com.dbondarenko.shpp.notes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NoteFragment extends Fragment {

    private static final String LOG_TAG = NoteFragment.class.getSimpleName();

    public NoteFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView()");
        return inflater.inflate(R.layout.fragment_note, container, false);
    }
}
