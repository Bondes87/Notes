package com.dbondarenko.shpp.notes.ui.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dbondarenko.shpp.notes.Constants;
import com.dbondarenko.shpp.notes.R;
import com.dbondarenko.shpp.notes.ui.adapters.NoteAdapter;
import com.dbondarenko.shpp.notes.ui.fragments.base.BaseFragment;
import com.dbondarenko.shpp.notes.ui.listeners.OnEmptyListListener;
import com.dbondarenko.shpp.notes.ui.listeners.OnListItemClickListener;

public class NotesListFragment extends BaseFragment implements View.OnClickListener,
        OnEmptyListListener, OnListItemClickListener {

    FloatingActionButton floatingActionButtonAddNote;
    RecyclerView recyclerViewNotesList;
    ProgressBar progressBarNotesLoading;
    TextView textViewNoNotes;

    NoteAdapter noteAdapter;

    public NotesListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View getContentView(LayoutInflater inflater, @Nullable ViewGroup container,
                               @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "getContentView");
        return inflater.inflate(R.layout.fragment_notes_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated");
        initViews(view);
        initActionBar();
        initRecyclerView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.floatingActionButtonAddNote:
                showNoteFragment(new NoteFragment());
        }
    }

    @Override
    public void onEmptyList(boolean isEmptyList) {
        Log.d(TAG, "onEmptyList()");
        if (isEmptyList) {
            textViewNoNotes.setVisibility(View.VISIBLE);
        } else {
            textViewNoNotes.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClickListItem(int position) {
        Log.d(TAG, "onClickListItem()");
        NoteFragment noteFragment = new NoteFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.KEY_NOTE, (Parcelable) noteAdapter.getNote(position));
        bundle.putInt(Constants.KEY_NOTE_POSITION, position);
        noteFragment.setArguments(bundle);
        showNoteFragment(noteFragment);
    }

    public void initViews(View view) {
        floatingActionButtonAddNote = view.findViewById(R.id.floatingActionButtonAddNote);
        floatingActionButtonAddNote.setOnClickListener(this);
        recyclerViewNotesList = view.findViewById(R.id.recyclerViewNotesList);
        progressBarNotesLoading = view.findViewById(R.id.progressBarNotesLoading);
        textViewNoNotes = view.findViewById(R.id.textViewNoNotes);
    }

    private void showNoteFragment(NoteFragment noteFragment) {
        Log.d(TAG, "showNoteFragment()");
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayoutContainerForContent, noteFragment)
                .addToBackStack(null)
                .commit();
    }

    private void initActionBar() {
        Log.d(TAG, "initActionBar()");
        ActionBar actionBar = ((AppCompatActivity) getContext()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setTitle(getString(R.string.app_name));
        }
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView()");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewNotesList.setLayoutManager(linearLayoutManager);
        initRecyclerViewAdapter();
        recyclerViewNotesList.setAdapter(noteAdapter);
    }

    private void initRecyclerViewAdapter() {
        if (noteAdapter == null) {
            noteAdapter = new NoteAdapter(
                    NotesListFragment.this,
                    NotesListFragment.this);
            progressBarNotesLoading.setVisibility(View.VISIBLE);
        }
    }
}