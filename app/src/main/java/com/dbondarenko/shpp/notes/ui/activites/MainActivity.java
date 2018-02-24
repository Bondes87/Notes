package com.dbondarenko.shpp.notes.ui.activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
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
import com.dbondarenko.shpp.notes.models.NoteModel;
import com.dbondarenko.shpp.notes.ui.activites.base.BaseActivity;
import com.dbondarenko.shpp.notes.ui.adapters.NoteAdapter;
import com.dbondarenko.shpp.notes.ui.listeners.OnEmptyListListener;
import com.dbondarenko.shpp.notes.ui.listeners.OnEndlessRecyclerScrollListener;
import com.dbondarenko.shpp.notes.ui.listeners.OnListItemClickListener;
import com.dbondarenko.shpp.notes.ui.loaders.ApiServiceAsyncTaskLoader;
import com.dbondarenko.shpp.notes.ui.widgets.MarginDecoration;

import java.util.ArrayList;
import java.util.List;

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

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        initActionBar();
        if (savedInstanceState != null) {
            initRecyclerView((ArrayList<NoteModel>) savedInstanceState.getSerializable(Constants.KEY_NOTES_LIST));
            totalAmountOfNotesOnServer = savedInstanceState.getInt(Constants.KEY_TOTAL_AMOUNT_OF_NOTES_ON_SERVER);
        } else {
            initRecyclerView(null);
        }
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick()");
        switch (v.getId()) {
            case R.id.floatingActionButtonAddNote:
                startActivityForResult(new Intent(this, NoteActivity.class), Constants.REQUEST_CODE_NOTE_ACTIVITY);
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
        startActivityForResult(intentToStartNoteActivity, Constants.REQUEST_CODE_NOTE_ACTIVITY);
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
                                    Constants.MAXIMUM_COUNT_OF_NOTES_TO_LOAD)));
                }
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<ApiLoaderResponse> loader, ApiLoaderResponse data) {
        Log.d(TAG, "onLoadFinished()");
        progressBarNotesLoading.setVisibility(View.GONE);
        noteAdapter.setEnabledFooter(false);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.REQUEST_CODE_NOTE_ACTIVITY:
                switch (resultCode) {

                    case Constants.RESULT_CODE_ADD_NOTE:
                        totalAmountOfNotesOnServer++;
                        if (((LinearLayoutManager) recyclerViewNotesList.getLayoutManager())
                                .findFirstCompletelyVisibleItemPosition() == 0) {
                            recyclerViewNotesList.scrollToPosition(0);
                        }
                        noteAdapter.addNote((NoteModel) data.getSerializableExtra(Constants.KEY_NOTE));
                        break;

                    case Constants.RESULT_CODE_UPDATE_NOTE:
                        noteAdapter.updateNote((NoteModel) data.getSerializableExtra(Constants.KEY_NOTE),
                                data.getIntExtra(Constants.KEY_NOTE_POSITION, -1));
                        break;

                    case Constants.RESULT_CODE_DELETE_NOTE:
                        totalAmountOfNotesOnServer--;
                        noteAdapter.deleteNote(data.getIntExtra(Constants.KEY_NOTE_POSITION, -1));
                        break;
                }
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState()");
        outState.putSerializable(Constants.KEY_NOTES_LIST, (ArrayList<NoteModel>) noteAdapter.getNotes());
        outState.putInt(Constants.KEY_TOTAL_AMOUNT_OF_NOTES_ON_SERVER, totalAmountOfNotesOnServer);
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

    private void initRecyclerView(List<NoteModel> notesList) {
        Log.d(TAG, "initRecyclerView()");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewNotesList.setLayoutManager(linearLayoutManager);
        recyclerViewNotesList.setItemAnimator(new DefaultItemAnimator());
        recyclerViewNotesList.addItemDecoration(new MarginDecoration(getApplicationContext()));
        initRecyclerViewAdapter(notesList);
        recyclerViewNotesList.setAdapter(noteAdapter);
        recyclerViewNotesList.addOnScrollListener(getEndlessRecyclerScrollListener());
    }

    @NonNull
    private OnEndlessRecyclerScrollListener getEndlessRecyclerScrollListener() {
        return new OnEndlessRecyclerScrollListener() {
            @Override
            public void onLoadMore() {
                if (totalAmountOfNotesOnServer > noteAdapter.getItemCount()) {
                    downloadNotes(noteAdapter.getItemCount());
                    noteAdapter.setEnabledFooter(true);
                }
            }

            @Override
            public void showFloatingActionButtonAddNote() {
                floatingActionButtonAddNote.animate()
                        .translationY(0)
                        .alpha(1)
                        .setInterpolator(new DecelerateInterpolator())
                        .start();
            }

            @Override
            public void hideFloatingActionButtonAddNote() {
                floatingActionButtonAddNote.animate()
                        .y(((View) floatingActionButtonAddNote.getParent()).getHeight())
                        .alpha(0)
                        .setInterpolator(new AccelerateInterpolator())
                        .start();
            }
        };
    }

    private void initRecyclerViewAdapter(List<NoteModel> notesList) {
        Log.d(TAG, "initRecyclerViewAdapter()");
        noteAdapter = new NoteAdapter(this, this);
        if (notesList == null) {
            progressBarNotesLoading.setVisibility(View.VISIBLE);
            initLoader();
        } else {
            noteAdapter.addNotes(notesList);
            noteAdapter.checkListForEmptiness();
        }
    }

    private void initLoader() {
        Log.d(TAG, "initLoader()");
        downloadNotes(0);
    }

    private void downloadNotes(int startPosition) {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.KEY_START_NOTE_POSITION, startPosition);
        getSupportLoaderManager().restartLoader(Constants.LOADER_ID_API_SERVICE, bundle, this);
    }
}