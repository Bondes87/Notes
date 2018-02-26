package com.dbondarenko.shpp.notes.ui.activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dbondarenko.shpp.notes.Constants;
import com.dbondarenko.shpp.notes.R;
import com.dbondarenko.shpp.notes.api.ApiLoaderResponse;
import com.dbondarenko.shpp.notes.api.ApiName;
import com.dbondarenko.shpp.notes.api.request.AddNoteRequest;
import com.dbondarenko.shpp.notes.api.request.DeleteNoteRequest;
import com.dbondarenko.shpp.notes.api.request.GetNotesRequest;
import com.dbondarenko.shpp.notes.api.request.base.BaseRequest;
import com.dbondarenko.shpp.notes.api.request.models.AddNoteRequestModel;
import com.dbondarenko.shpp.notes.api.request.models.DeleteNoteRequestModel;
import com.dbondarenko.shpp.notes.api.request.models.GetNotesRequestModel;
import com.dbondarenko.shpp.notes.api.response.model.DeleteNoteResultModel;
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
import com.dbondarenko.shpp.notes.ui.widgets.RecyclerItemTouchHelper;

import java.util.ArrayList;
import java.util.List;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

public class MainActivity extends BaseActivity implements View.OnClickListener,
        OnEmptyListListener, OnListItemClickListener, LoaderManager.LoaderCallbacks<ApiLoaderResponse> {


    private FloatingActionButton floatingActionButtonAddNote;
    private RecyclerView recyclerViewNotesList;
    private SmoothProgressBar smoothProgressBarNotesLoading;
    private ProgressBar progressBarActionsWithNote;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected()");
        switch (item.getItemId()) {

            case R.id.itemRefresh:
                progressBarActionsWithNote.setVisibility(View.VISIBLE);
                textViewNoNotes.setVisibility(View.GONE);
                noteAdapter.clearNotesFromAdapter();
                totalAmountOfNotesOnServer = 0;
                updateRecyclerViewNotesListListener();
                downloadNotes(noteAdapter.getItemCount());
                return true;

            default:
                return super.onOptionsItemSelected(item);
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
                            (BaseRequest) args.getSerializable(Constants.KEY_REQUEST));
                }
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<ApiLoaderResponse> loader, ApiLoaderResponse data) {
        Log.d(TAG, "onLoadFinished()");
        progressBarActionsWithNote.setVisibility(View.GONE);
        smoothProgressBarNotesLoading.setVisibility(View.GONE);
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
                    showMessageInSnackbar(recyclerViewNotesList, getString(R.string.error_server_is_not_responding));
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

            case DELETE_DELETE_NOTE:
                DeleteNoteResultModel deleteNoteResultModel = (DeleteNoteResultModel) baseResultModel;
                if (deleteNoteResultModel.isDeleted() &&
                        totalAmountOfNotesOnServer > 0 &&
                        ((LinearLayoutManager) recyclerViewNotesList.getLayoutManager())
                                .findLastVisibleItemPosition() == noteAdapter.getItemCount()) {
                    smoothProgressBarNotesLoading.setVisibility(View.VISIBLE);
                    downloadNotes(noteAdapter.getItemCount());
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult()");
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

    private void updateRecyclerViewNotesListListener() {
        recyclerViewNotesList.addOnScrollListener(getEndlessRecyclerScrollListener());
    }

    private void initViews() {
        Log.d(TAG, "initViews()");
        floatingActionButtonAddNote = findViewById(R.id.floatingActionButtonAddNote);
        floatingActionButtonAddNote.setOnClickListener(this);
        recyclerViewNotesList = findViewById(R.id.recyclerViewNotesList);
        smoothProgressBarNotesLoading = findViewById(R.id.smoothProgressBarNotesLoading);
        progressBarActionsWithNote = findViewById(R.id.progressBarActionsWithNote);
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
        new ItemTouchHelper(getRecyclerItemTouchHelper()).attachToRecyclerView(recyclerViewNotesList);
    }

    @NonNull
    private RecyclerItemTouchHelper getRecyclerItemTouchHelper() {
        Log.d(TAG, "getRecyclerItemTouchHelper()");
        return new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT,
                (viewHolder, direction, position) -> {
                    NoteModel note = noteAdapter.getNote(position);
                    totalAmountOfNotesOnServer--;
                    noteAdapter.deleteNote(position);
                    deleteNote(note);
                    reportOnDeletingNote(note, position);
                });
    }

    private void deleteNote(NoteModel note) {
        Log.d(TAG, "deleteNote()");
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.KEY_REQUEST,
                new DeleteNoteRequest(new DeleteNoteRequestModel(note.getDatetime())));
        getSupportLoaderManager().restartLoader(Constants.LOADER_ID_API_SERVICE, bundle, this);
    }

    private void reportOnDeletingNote(NoteModel note, int position) {
        Log.d(TAG, "reportOnDeletingNote()");
        Snackbar snackbar = Snackbar.make(recyclerViewNotesList, getString(R.string.text_note_deleted),
                Snackbar.LENGTH_SHORT);
        snackbar.setAction(getString(R.string.button_cancel), cancelButton -> {
            totalAmountOfNotesOnServer++;
            noteAdapter.addNote(note, position);
            if (isStartOrEndNotePosition(position)) {
                recyclerViewNotesList.scrollToPosition(position);
            }
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.KEY_REQUEST, new AddNoteRequest(new AddNoteRequestModel(note)));
            getSupportLoaderManager().restartLoader(Constants.LOADER_ID_API_SERVICE, bundle, this);
        });
        snackbar.setActionTextColor(getResources().getColor(R.color.colorAccent));
        snackbar.show();
    }

    private boolean isStartOrEndNotePosition(int currentPosition) {
        return currentPosition == 0 || currentPosition == noteAdapter.getItemCount() - 1;
    }

    @NonNull
    private OnEndlessRecyclerScrollListener getEndlessRecyclerScrollListener() {
        Log.d(TAG, "getEndlessRecyclerScrollListener()");
        return new OnEndlessRecyclerScrollListener() {

            @Override
            public void onLoadMore() {
                Log.d(TAG, "onLoadMore()");
                if (totalAmountOfNotesOnServer > noteAdapter.getItemCount()) {
                    smoothProgressBarNotesLoading.setVisibility(View.VISIBLE);
                    downloadNotes(noteAdapter.getItemCount());
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
            progressBarActionsWithNote.setVisibility(View.VISIBLE);
            downloadNotes(0);
        } else {
            noteAdapter.addNotes(notesList);
            noteAdapter.checkListForEmptiness();
        }
    }

    private void downloadNotes(int startPosition) {
        Log.d(TAG, "downloadNotes()");
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.KEY_REQUEST, new GetNotesRequest(new GetNotesRequestModel(
                startPosition, Constants.MAXIMUM_COUNT_OF_NOTES_TO_LOAD)));
        getSupportLoaderManager().restartLoader(Constants.LOADER_ID_API_SERVICE, bundle, this);
    }
}