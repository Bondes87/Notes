package com.dbondarenko.shpp.notes.ui.activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dbondarenko.shpp.notes.Constants;
import com.dbondarenko.shpp.notes.R;
import com.dbondarenko.shpp.notes.api.ApiLoaderResponse;
import com.dbondarenko.shpp.notes.api.ApiName;
import com.dbondarenko.shpp.notes.api.request.GetNotesRequest;
import com.dbondarenko.shpp.notes.api.request.models.GetNotesRequestModel;
import com.dbondarenko.shpp.notes.api.response.model.GetNotesResultModel;
import com.dbondarenko.shpp.notes.api.response.model.base.BaseErrorModel;
import com.dbondarenko.shpp.notes.api.response.model.base.BaseResultModel;
import com.dbondarenko.shpp.notes.ui.activites.base.BaseActivity;
import com.dbondarenko.shpp.notes.ui.adapters.NoteAdapter;
import com.dbondarenko.shpp.notes.ui.listeners.OnEmptyListListener;
import com.dbondarenko.shpp.notes.ui.listeners.OnListItemClickListener;
import com.dbondarenko.shpp.notes.ui.loaders.ApiServiceAsyncTaskLoader;
import com.dbondarenko.shpp.notes.ui.widgets.MarginDecoration;

public class MainActivity extends BaseActivity implements View.OnClickListener,
        OnEmptyListListener, OnListItemClickListener, LoaderManager.LoaderCallbacks<ApiLoaderResponse> {

    private FloatingActionButton floatingActionButtonAddNote;
    private RecyclerView recyclerViewNotesList;
    private ProgressBar progressBarNotesLoading;
    private TextView textViewNoNotes;

    private NoteAdapter noteAdapter;

    private int totalAmountOfNotesOnServer;

    @Override
    public int getContentViewResourceId() {
        return R.layout.activity_main;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        initActionBar();
        initRecyclerView();
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick()");
        switch (v.getId()) {
            case R.id.floatingActionButtonAddNote:
                startActivity(new Intent(this, NoteActivity.class));
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
        Intent intentToStartNoteActivity = new Intent(this, NoteActivity.class);
        intentToStartNoteActivity.putExtra(Constants.KEY_NOTE, noteAdapter.getNote(position));
        intentToStartNoteActivity.putExtra(Constants.KEY_NOTE_POSITION, position);
        startActivity(intentToStartNoteActivity);
    }

    @Override
    public Loader<ApiLoaderResponse> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader() " + id);
        switch (id) {
            case Constants.LOADER_ID_API_SERVICE:
                if (args != null) {
                    return new ApiServiceAsyncTaskLoader(getApplicationContext(),
                            new GetNotesRequest(new GetNotesRequestModel(
                                    args.getInt(Constants.KEY_START_NOTE_POSITION),
                                    Constants.AMOUNT_OF_NOTES_TO_DOWNLOAD)));
                }
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<ApiLoaderResponse> loader, ApiLoaderResponse data) {
        Log.d(TAG, "onLoadFinished()");
        progressBarNotesLoading.setVisibility(View.GONE);
        if (data != null) {
            if (data.getResponseModel() != null) {
                if (data.getResponseModel().getResult() != null) {
                    handleSuccessResult(data.getApiName(), data.getResponseModel().getResult());
                } else {
                    if (data.getResponseModel().getError() != null) {
                        handleFailureResult(data.getResponseModel().getError());
                    }
                }
            } else {
                if (data.getException() != null) {
                    showMessageInSnackbar(recyclerViewNotesList, getString(R.string.error_loading_data));
                }
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<ApiLoaderResponse> loader) {
        Log.d(TAG, "onLoaderReset()");
    }

    @Override
    public void handleFailureResult(BaseErrorModel baseErrorModel) {
        Log.d(TAG, "handleFailureResult()");
        String message = baseErrorModel.getMessage();
        if (!TextUtils.isEmpty(message)) {
            showMessageInSnackbar(recyclerViewNotesList, message);
        }
    }

    @Override
    public void handleSuccessResult(ApiName apiName, BaseResultModel baseResultModel) {
        Log.d(TAG, "handleSuccessResult()");
        switch (apiName) {
            case GET_GET_NOTES:
                GetNotesResultModel getNotesResultModel = (GetNotesResultModel) baseResultModel;
                if (getNotesResultModel.getNotes() != null) {
                    noteAdapter.addNotes(getNotesResultModel.getNotes());
                }
                totalAmountOfNotesOnServer = getNotesResultModel.getTotalAmountOfNotesOnServer();
                if (totalAmountOfNotesOnServer == 0) {
                    textViewNoNotes.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    private void initViews() {
        Log.d(TAG, "initViews()");
        floatingActionButtonAddNote = findViewById(R.id.floatingActionButtonAddNote);
        floatingActionButtonAddNote.setOnClickListener(this);
        recyclerViewNotesList = findViewById(R.id.recyclerViewNotesList);
        progressBarNotesLoading = findViewById(R.id.progressBarNotesLoading);
        textViewNoNotes = findViewById(R.id.textViewNoNotes);
    }

    private void initActionBar() {
        Log.d(TAG, "initActionBar()");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setTitle(getString(R.string.app_name));
        }
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView()");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewNotesList.setLayoutManager(linearLayoutManager);
        recyclerViewNotesList.setItemAnimator(new DefaultItemAnimator());
        recyclerViewNotesList.addItemDecoration(new MarginDecoration(getApplicationContext()));
        initRecyclerViewAdapter();
        recyclerViewNotesList.setAdapter(noteAdapter);
    }

    private void initRecyclerViewAdapter() {
        Log.d(TAG, "initRecyclerViewAdapter()");
        if (noteAdapter == null) {
            noteAdapter = new NoteAdapter(this, this);
            progressBarNotesLoading.setVisibility(View.VISIBLE);
            initLoader();
        } else {
            noteAdapter.checkListForEmptiness();
        }
    }

    private void initLoader() {
        Log.d(TAG, "initLoader()");
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.KEY_START_NOTE_POSITION, noteAdapter.getItemCount());
        getSupportLoaderManager().initLoader(Constants.LOADER_ID_API_SERVICE, bundle, this);
    }
}