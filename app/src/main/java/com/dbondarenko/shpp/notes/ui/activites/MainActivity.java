package com.dbondarenko.shpp.notes.ui.activites;

import android.os.Bundle;

import com.dbondarenko.shpp.notes.R;
import com.dbondarenko.shpp.notes.ui.activites.base.BaseActivity;
import com.dbondarenko.shpp.notes.ui.fragments.NotesListFragment;

public class MainActivity extends BaseActivity {

    @Override
    public int getContentViewResourceId() {
        return R.layout.activity_main;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.frameLayoutContainerForContent, new NotesListFragment())
                    .commit();
        }
    }
}