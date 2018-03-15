package com.dbondarenko.shpp.notes.ui.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.dbondarenko.shpp.notes.Constants;
import com.dbondarenko.shpp.notes.R;
import com.dbondarenko.shpp.notes.models.api.ApiLoaderResponse;
import com.dbondarenko.shpp.notes.api.ApiNameAnnotation;
import com.dbondarenko.shpp.notes.models.api.request.AddNoteRequest;
import com.dbondarenko.shpp.notes.models.api.request.DeleteNoteRequest;
import com.dbondarenko.shpp.notes.models.api.request.UpdateNoteRequest;
import com.dbondarenko.shpp.notes.models.api.request.base.BaseRequest;
import com.dbondarenko.shpp.notes.models.api.request.models.AddNoteRequestModel;
import com.dbondarenko.shpp.notes.models.api.request.models.DeleteNoteRequestModel;
import com.dbondarenko.shpp.notes.models.api.request.models.UpdateNoteRequestModel;
import com.dbondarenko.shpp.core.models.responses.models.AddNoteResultModel;
import com.dbondarenko.shpp.core.models.responses.models.DeleteNoteResultModel;
import com.dbondarenko.shpp.core.models.responses.models.UpdateNoteResultModel;
import com.dbondarenko.shpp.core.models.responses.models.base.BaseErrorModel;
import com.dbondarenko.shpp.core.models.responses.models.base.BaseResultModel;
import com.dbondarenko.shpp.notes.models.exception.NoInternetConnectionException;
import com.dbondarenko.shpp.core.models.NoteModel;
import com.dbondarenko.shpp.notes.ui.activites.base.BaseActivity;
import com.dbondarenko.shpp.notes.ui.fragments.ActionDialogFragment;
import com.dbondarenko.shpp.notes.ui.listeners.OnResultDialogListener;
import com.dbondarenko.shpp.notes.ui.loaders.ApiServiceAsyncTaskLoader;
import com.dbondarenko.shpp.notes.utils.Util;

import java.util.Calendar;

public class NoteActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<ApiLoaderResponse>,
        OnResultDialogListener {

    private EditText editTextMessage;
    private ProgressBar progressBarActionsWithNote;

    private NoteModel note;
    private long datetime;
    private int notePosition;

    @Override
    public int getContentViewResourceId() {
        return R.layout.activity_note;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        note = (NoteModel) getIntent().getSerializableExtra(Constants.KEY_NOTE);
        notePosition = getIntent().getIntExtra(Constants.KEY_NOTE_POSITION, -1);
        if (savedInstanceState != null) {
            note = (NoteModel) savedInstanceState.getSerializable(Constants.KEY_NOTE);
            setRequest((BaseRequest) savedInstanceState.getSerializable(Constants.KEY_REQUEST));
        }
        initViews();
        fillViews();
        showSoftKeyboard();
        if (getRequest() != null) {
            showSnackbar(editTextMessage, getString(R.string.error_no_connection),
                    getString(R.string.button_repeat), listener -> {
                        getSupportLoaderManager().restartLoader(Constants.LOADER_ID_API_SERVICE, null,
                                this);
                    });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu()");
        getMenuInflater().inflate(R.menu.activity_note_menu, menu);
        if (notePosition == -1) {
            menu.findItem(R.id.itemDeleteNotes).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected()");
        String message = editTextMessage.getText().toString();
        switch (item.getItemId()) {

            case R.id.itemSaveNote:
                if (TextUtils.isEmpty(message)) {
                    showSnackbar(editTextMessage, getString(R.string.error_note_is_empty));
                    return true;
                }
                hideSoftKeyboard();
                saveNote(message);
                return true;

            case R.id.itemDeleteNotes:
                hideSoftKeyboard();
                showDeleteNoteDialogFragment();
                return true;

            case android.R.id.home:
                setRequest(null);
                hideSoftKeyboard();
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Loader<ApiLoaderResponse> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader() " + id);
        switch (id) {
            case Constants.LOADER_ID_API_SERVICE:
                if (getRequest() != null) {
                    progressBarActionsWithNote.setVisibility(View.VISIBLE);
                    editTextMessage.setVisibility(View.GONE);
                    return new ApiServiceAsyncTaskLoader(getApplicationContext(), getRequest());
                }
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<ApiLoaderResponse> loader, ApiLoaderResponse data) {
        Log.d(TAG, "onLoadFinished()");
        progressBarActionsWithNote.setVisibility(View.GONE);
        if (data != null) {
            if (data.getResponseModel() != null) {
                setRequest(null);
                if (data.getResponseModel().getResult() != null) {
                    handleSuccessResult(data.getApiNameAnnotation(), data.getResponseModel().getResult());
                } else {
                    if (data.getResponseModel().getError() != null) {
                        editTextMessage.setVisibility(View.VISIBLE);
                        handleFailureResult(data.getResponseModel().getError());
                    }
                }
            } else {
                if (data.getException() != null) {
                    editTextMessage.setVisibility(View.VISIBLE);
                    if (data.getException() instanceof NoInternetConnectionException) {
                        showSnackbar(editTextMessage, data.getException().getMessage(),
                                getString(R.string.button_repeat), listener -> {
                                    getSupportLoaderManager().restartLoader(Constants.LOADER_ID_API_SERVICE, null,
                                            this);
                                });
                    } else {
                        showSnackbar(editTextMessage, getString(R.string.error_server_is_not_responding));
                    }
                }
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<ApiLoaderResponse> loader) {

    }

    @Override
    public void handleSuccessResult(ApiNameAnnotation apiNameAnnotation, BaseResultModel baseResultModel) {
        Log.d(TAG, "handleSuccessResult()");
        switch (apiNameAnnotation.getApiName()) {

            case ApiNameAnnotation.POST_ADD_NOTE:
                AddNoteResultModel addNoteResultModel = (AddNoteResultModel) baseResultModel;
                if (addNoteResultModel.isAdded()) {
                    showToast(getApplicationContext(), getString(R.string.text_note_added));
                    returnResult(Constants.RESULT_CODE_ADD_NOTE);
                }
                break;

            case ApiNameAnnotation.PUT_UPDATE_NOTE:
                UpdateNoteResultModel updateNoteResultModel = (UpdateNoteResultModel) baseResultModel;
                if (updateNoteResultModel.isUpdated()) {
                    showToast(getApplicationContext(), getString(R.string.text_note_updated));
                    returnResult(Constants.RESULT_CODE_UPDATE_NOTE);
                }
                break;

            case ApiNameAnnotation.DELETE_DELETE_NOTE:
                DeleteNoteResultModel deleteNoteResultModel = (DeleteNoteResultModel) baseResultModel;
                if (deleteNoteResultModel.isDeleted()) {
                    showToast(getApplicationContext(), getString(R.string.text_note_deleted));
                    returnResult(Constants.RESULT_CODE_DELETE_NOTE);
                }
                break;
        }
    }

    @Override
    public void handleFailureResult(BaseErrorModel baseErrorModel) {
        Log.d(TAG, "handleFailureResult()");
        String message = baseErrorModel.getMessage();
        if (!TextUtils.isEmpty(message)) {
            showSnackbar(editTextMessage, message);
        }
    }

    @Override
    public void onDialogPositiveClicked(String dialogFragmentTag) {
        Log.d(TAG, "onDialogPositiveClicked()");
        switch (dialogFragmentTag) {
            case Constants.TAG_DELETE_NOTE_DIALOG_FRAGMENT:
                deleteNote();
                break;
        }
    }

    @Override
    public void onDialogNegativeClicked(String dialogFragmentTag) {
        Log.d(TAG, "onDialogNegativeClicked()");
        switch (dialogFragmentTag) {
            case Constants.TAG_DELETE_NOTE_DIALOG_FRAGMENT:
                showSoftKeyboard();
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState()");
        outState.putSerializable(Constants.KEY_NOTE, new NoteModel(datetime, editTextMessage.getText().toString()));
        if (getRequest() != null) {
            outState.putSerializable(Constants.KEY_REQUEST, getRequest());
        }
    }

    private void showDeleteNoteDialogFragment() {
        Log.d(TAG, "showDeleteNoteDialogFragment()");
        ActionDialogFragment deleteNoteDialogFragmentFrag = ActionDialogFragment.newInstance(
                getString(R.string.text_delete_note),
                getString(R.string.text_for_dialog_to_delete_note),
                getString(R.string.button_delete),
                getString(R.string.button_cancel));
        deleteNoteDialogFragmentFrag.show(getSupportFragmentManager(), Constants.TAG_DELETE_NOTE_DIALOG_FRAGMENT);
    }

    private void returnResult(int resultCode) {
        Log.d(TAG, "returnResult()");
        Intent intent = new Intent();
        intent.putExtra(Constants.KEY_NOTE, note);
        intent.putExtra(Constants.KEY_NOTE_POSITION, notePosition);
        setResult(resultCode, intent);
        finish();
    }

    private void fillViews() {
        Log.d(TAG, "fillViews()");
        if (note != null) {
            datetime = note.getDatetime();
            editTextMessage.setText(note.getMessage());
            editTextMessage.setSelection(note.getMessage().length());
        } else {
            datetime = Calendar.getInstance().getTimeInMillis();
        }
        setActionBarTitle(Util.getStringDatetime(datetime));
    }

    private void saveNote(String message) {
        Log.d(TAG, "saveNote()");
        createRequest(message);
        getSupportLoaderManager().restartLoader(Constants.LOADER_ID_API_SERVICE, null, this);
    }

    private void deleteNote() {
        Log.d(TAG, "deleteNote()");
        if (note != null) {
            setRequest(new DeleteNoteRequest(new DeleteNoteRequestModel(note.getDatetime())));
            getSupportLoaderManager().restartLoader(Constants.LOADER_ID_API_SERVICE, null, this);
        }
    }

    private void createRequest(String message) {
        Log.d(TAG, "createRequest()");
        if (notePosition == -1) {
            note = new NoteModel(datetime, message);
            setRequest(new AddNoteRequest(new AddNoteRequestModel(note)));
        } else {
            note.setMessage(message);
            setRequest(new UpdateNoteRequest(new UpdateNoteRequestModel(note)));
        }
    }

    private void hideSoftKeyboard() {
        Log.d(TAG, "hideSoftKeyboard()");
        InputMethodManager inputMethodManager =
                (InputMethodManager) getApplicationContext()
                        .getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (editTextMessage != null && inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(editTextMessage.getWindowToken(), 0);
        }
    }

    private void showSoftKeyboard() {
        Log.d(TAG, "showSoftKeyboard()");
        InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext()
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    private void setActionBarTitle(String title) {
        Log.d(TAG, "setActionBarTitle()");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initViews() {
        Log.d(TAG, "initViews()");
        editTextMessage = findViewById(R.id.editTextMessage);
        editTextMessage.requestFocus();
        progressBarActionsWithNote = findViewById(R.id.progressBarActionsWithNote);
    }
}