package com.dbondarenko.shpp.notes.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.dbondarenko.shpp.notes.Constants;
import com.dbondarenko.shpp.notes.R;
import com.dbondarenko.shpp.notes.api.ApiName;
import com.dbondarenko.shpp.notes.api.response.model.base.BaseErrorModel;
import com.dbondarenko.shpp.notes.api.response.model.base.BaseResultModel;
import com.dbondarenko.shpp.notes.models.NoteModel;
import com.dbondarenko.shpp.notes.ui.fragments.base.BaseFragment;
import com.dbondarenko.shpp.notes.utils.Util;

import java.util.Calendar;

public class NoteFragment extends BaseFragment {

    private EditText editTextMessage;
    private ProgressBar progressBarActionsWithNote;

    private NoteModel note;
    private long datetime;
    private int notePosition;

    public NoteFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            note = bundle.getParcelable(Constants.KEY_NOTE);
            notePosition = bundle.getInt(Constants.KEY_NOTE_POSITION);
        }
        ActionBar actionBar = ((AppCompatActivity) getContext()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View getContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        Log.d(TAG, "getContentView()");
        return inflater.inflate(R.layout.fragment_note, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        if (note != null) {
            setActionBarTitle(Util.getStringDatetime(note.getDatetime()));
            editTextMessage.setText(note.getMessage());
        } else {
            datetime = Calendar.getInstance().getTimeInMillis();
            setActionBarTitle(Util.getStringDatetime(datetime));
        }
        editTextMessage.requestFocus();
        showSoftKeyboard();
    }

    @Override
    public void handleSuccessResult(ApiName apiName, BaseResultModel baseResultModel) {
        Log.d(TAG, "handleSuccessResult()");
    }

    @Override
    public void handleFailureResult(BaseErrorModel baseErrorModel) {
        Log.d(TAG, "handleFailureResult()");
    }

    public void showSoftKeyboard() {
        Log.d(TAG, "showSoftKeyboard()");
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getApplicationContext()
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    private void setActionBarTitle(String title) {
        ActionBar actionBar = ((AppCompatActivity) getContext()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    private void initViews(View view) {
        editTextMessage = view.findViewById(R.id.editTextMessage);
        progressBarActionsWithNote = view.findViewById(R.id.progressBarActionsWithNote);
    }
}