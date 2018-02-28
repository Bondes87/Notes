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
import android.view.ActionMode;
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
import com.dbondarenko.shpp.notes.api.request.DeleteNotesRequest;
import com.dbondarenko.shpp.notes.api.request.GetNotesRequest;
import com.dbondarenko.shpp.notes.api.request.base.BaseRequest;
import com.dbondarenko.shpp.notes.api.request.models.DeleteNotesRequestModel;
import com.dbondarenko.shpp.notes.api.request.models.GetNotesRequestModel;
import com.dbondarenko.shpp.notes.api.response.model.DeleteNotesResultModel;
import com.dbondarenko.shpp.notes.api.response.model.GetNotesResultModel;
import com.dbondarenko.shpp.notes.api.response.model.base.BaseErrorModel;
import com.dbondarenko.shpp.notes.api.response.model.base.BaseResultModel;
import com.dbondarenko.shpp.notes.models.NoInternetConnectionException;
import com.dbondarenko.shpp.notes.models.NoteModel;
import com.dbondarenko.shpp.notes.ui.activites.base.BaseActivity;
import com.dbondarenko.shpp.notes.ui.adapters.NoteAdapter;
import com.dbondarenko.shpp.notes.ui.listeners.OnEmptyListListener;
import com.dbondarenko.shpp.notes.ui.listeners.OnEndlessRecyclerScrollListener;
import com.dbondarenko.shpp.notes.ui.loaders.ApiServiceAsyncTaskLoader;
import com.dbondarenko.shpp.notes.ui.widgets.MarginDecoration;

import java.util.ArrayList;
import java.util.List;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

public class MainActivity extends BaseActivity implements View.OnClickListener,
        View.OnLongClickListener, OnEmptyListListener,
        LoaderManager.LoaderCallbacks<ApiLoaderResponse>, ActionMode.Callback {

    private FloatingActionButton floatingActionButtonAddNote;
    private RecyclerView recyclerViewNotesList;
    private SmoothProgressBar smoothProgressBarNotesLoading;
    private ProgressBar progressBarActionsWithNote;
    private TextView textViewNoNotes;

    private ActionMode multiSelectActionMode;
    private NoteAdapter noteAdapter;

    private boolean isMultiSelectActionModeActivated;
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
            totalAmountOfNotesOnServer = savedInstanceState.getInt(Constants.KEY_TOTAL_AMOUNT_OF_NOTES_ON_SERVER);
            baseRequest = (BaseRequest) savedInstanceState.getSerializable(Constants.KEY_REQUEST);
            initRecyclerView((ArrayList<NoteModel>) savedInstanceState.getSerializable(Constants.KEY_NOTES_LIST));
            List<Integer> multiSelectNotesPositions = (ArrayList<Integer>) savedInstanceState.getSerializable(Constants
                    .KEY_MULTI_SELECT_NOTES_POSITIONS);
            restoreMultiSelectActionMode(multiSelectNotesPositions);
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
                dismissSnackBar();
                setRequestParameters(null, true);
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
                setRequestParameters(null, true);
                startActivityForResult(new Intent(this, NoteActivity.class),
                        Constants.REQUEST_CODE_NOTE_ACTIVITY);
                dismissSnackBar();
                setRequestParameters(null, true);
                break;

            case R.id.itemContainerCardView:
                int position = recyclerViewNotesList.getChildAdapterPosition(v);
                if (isMultiSelectActionModeActivated) {
                    noteAdapter.addMultiSelectNote(position);
                    multiSelectActionMode.setTitle(getString(R.string.title_action_mode,
                            noteAdapter.getMultiSelectedCount()));
                } else {
                    Intent intentToStartNoteActivity = new Intent(this, NoteActivity.class);
                    intentToStartNoteActivity.putExtra(Constants.KEY_NOTE, noteAdapter.getNote(position));
                    intentToStartNoteActivity.putExtra(Constants.KEY_NOTE_POSITION, position);
                    setRequestParameters(null, true);
                    startActivityForResult(intentToStartNoteActivity, Constants.REQUEST_CODE_NOTE_ACTIVITY);
                }
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        Log.d(TAG, "onLongClick()");
        switch (v.getId()) {
            case R.id.itemContainerCardView:
                if (!isMultiSelectActionModeActivated) {
                    noteAdapter.addMultiSelectNote(recyclerViewNotesList.getChildAdapterPosition(v));
                    multiSelectActionModeActivated();
                    return true;
                }
            default:
                return false;
        }
    }

    @Override
    public Loader<ApiLoaderResponse> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader() " + id);
        switch (id) {
            case Constants.LOADER_ID_API_SERVICE:
                if (baseRequest != null) {
                    return new ApiServiceAsyncTaskLoader(getApplicationContext(), baseRequest);
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
                setRequestParameters(null, true);
                if (data.getResponseModel().getResult() != null) {
                    handleSuccessResult(data.getApiName(), data.getResponseModel().getResult());
                } else {
                    if (data.getResponseModel().getError() != null) {
                        handleFailureResult(data.getResponseModel().getError());
                    }
                }
            } else {
                if (data.getException() != null) {
                    if (data.getException() instanceof NoInternetConnectionException) {
                        showSnackbar(recyclerViewNotesList, data.getException().getMessage(),
                                getString(R.string.button_repeat), listener -> {
                                    getSupportLoaderManager().restartLoader(Constants.LOADER_ID_API_SERVICE,
                                            null, this);
                                });
                    } else {
                        showSnackbar(recyclerViewNotesList, getString(R.string.error_server_is_not_responding));
                    }
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
            showSnackbar(recyclerViewNotesList, message);
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

            case DELETE_DELETE_NOTES:
                DeleteNotesResultModel deleteNotesResultModel = (DeleteNotesResultModel) baseResultModel;
                if (deleteNotesResultModel.isDeleted()) {
                    noteAdapter.deleteMultiSelectNotes();
                    multiSelectActionMode.finish();
                    showToast(getApplicationContext(), getString(R.string.text_note_deleted));
                }
        }
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.multi_select_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        menu.findItem(R.id.itemDeleteNote).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemDeleteNote:
                if (noteAdapter.getMultiSelectedCount() != 0) {
                    deleteMultiSelectNotes();
                }
                return true;
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        isMultiSelectActionModeActivated = false;
        noteAdapter.clearMultiSelectNotes();
        multiSelectActionMode = null;
        floatingActionButtonAddNote.show();
        dismissSnackBar();
        setRequestParameters(null, true);
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
        if (baseRequest != null) {
            outState.putSerializable(Constants.KEY_REQUEST, baseRequest);
        }
        outState.putSerializable(Constants.KEY_MULTI_SELECT_NOTES_POSITIONS,
                (ArrayList<Integer>) noteAdapter.getMultiSelectNotesPositions());
    }

    private void deleteMultiSelectNotes() {
        List<NoteModel> multiSelectNotesList = noteAdapter.getMultiSelectNotes();
        if (!multiSelectNotesList.isEmpty()) {
            long[] datetimeArray = new long[multiSelectNotesList.size()];
            for (int i = 0; i < multiSelectNotesList.size(); i++) {
                datetimeArray[i] = multiSelectNotesList.get(i).getDatetime();
            }
            setRequestParameters(new DeleteNotesRequest(new DeleteNotesRequestModel(datetimeArray)), false);
            getSupportLoaderManager().restartLoader(Constants.LOADER_ID_API_SERVICE, null, this);
        }
    }

    private void multiSelectActionModeActivated() {
        isMultiSelectActionModeActivated = true;
        multiSelectActionMode = startActionMode(this);
        floatingActionButtonAddNote.hide();
        multiSelectActionMode.setTitle(getString(R.string.title_action_mode, noteAdapter.getMultiSelectedCount()));
    }

    private void restoreMultiSelectActionMode(List<Integer> multiSelectNotesPositions) {
        if (multiSelectNotesPositions != null && !multiSelectNotesPositions.isEmpty()) {
            multiSelectActionModeActivated();
            noteAdapter.addMultiSelectNotes(multiSelectNotesPositions);
            multiSelectActionMode.setTitle(getString(R.string.title_action_mode,
                    noteAdapter.getMultiSelectedCount()));
        }
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
        noteAdapter = new NoteAdapter(this, this, this);
        if (notesList == null) {
            progressBarActionsWithNote.setVisibility(View.VISIBLE);
            downloadNotes(0);
        } else {
            noteAdapter.addNotes(notesList);
            if (baseRequest != null) {
                showSnackbar(recyclerViewNotesList, getString(R.string.button_repeat),
                        getString(R.string.button_repeat), listener -> {
                            getSupportLoaderManager().restartLoader(Constants.LOADER_ID_API_SERVICE, null,
                                    this);
                        });
                onEmptyList(false);
            } else {
                noteAdapter.checkListForEmptiness();
            }
        }
    }

    private void downloadNotes(int startPosition) {
        Log.d(TAG, "downloadNotes()");
        setRequestParameters(new GetNotesRequest(new GetNotesRequestModel(
                startPosition, Constants.MAXIMUM_COUNT_OF_NOTES_TO_LOAD)), false);
        getSupportLoaderManager().restartLoader(Constants.LOADER_ID_API_SERVICE, null, this);
    }
}