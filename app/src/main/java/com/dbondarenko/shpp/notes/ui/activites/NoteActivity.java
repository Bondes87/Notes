package com.dbondarenko.shpp.notes.ui.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

import com.dbondarenko.shpp.notes.Constants;
import com.dbondarenko.shpp.notes.R;
import com.dbondarenko.shpp.notes.api.ApiLoaderResponse;
import com.dbondarenko.shpp.notes.api.ApiName;
import com.dbondarenko.shpp.notes.api.request.AddNoteRequest;
import com.dbondarenko.shpp.notes.api.request.DeleteNoteRequest;
import com.dbondarenko.shpp.notes.api.request.UpdateNoteRequest;
import com.dbondarenko.shpp.notes.api.request.base.BaseRequest;
import com.dbondarenko.shpp.notes.api.request.models.AddNoteRequestModel;
import com.dbondarenko.shpp.notes.api.request.models.DeleteNoteRequestModel;
import com.dbondarenko.shpp.notes.api.request.models.UpdateNoteRequestModel;
import com.dbondarenko.shpp.notes.api.response.model.AddNoteResultModel;
import com.dbondarenko.shpp.notes.api.response.model.DeleteNoteResultModel;
import com.dbondarenko.shpp.notes.api.response.model.UpdateNoteResultModel;
import com.dbondarenko.shpp.notes.api.response.model.base.BaseErrorModel;
import com.dbondarenko.shpp.notes.api.response.model.base.BaseResultModel;
import com.dbondarenko.shpp.notes.models.NoteModel;
import com.dbondarenko.shpp.notes.ui.activites.base.BaseActivity;
import com.dbondarenko.shpp.notes.ui.fragments.DeleteNoteDialogFragment;
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
        }
        initViews();
        fillViews();
        showSoftKeyboard();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fragment_note_menu, menu);
        if (notePosition == -1) {
            menu.findItem(R.id.itemDeleteNote).setVisible(false);
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
                    showMessageInSnackbar(editTextMessage, getString(R.string.error_note_is_empty));
                    return true;
                }
                hideSoftKeyboard();
                saveNote(message);
                return true;

            case R.id.itemDeleteNote:
                hideSoftKeyboard();
                showDeleteNoteDialogFragment();
                return true;

            case android.R.id.home:
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
                    showMessageInSnackbar(editTextMessage, getString(R.string.error_loading_data));
                }
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<ApiLoaderResponse> loader) {

    }

    @Override
    public void handleSuccessResult(ApiName apiName, BaseResultModel baseResultModel) {
        Log.d(TAG, "handleSuccessResult()");
        switch (apiName) {

            case POST_ADD_NOTE:
                AddNoteResultModel addNoteResultModel = (AddNoteResultModel) baseResultModel;
                if (addNoteResultModel.isAdded()) {
                    showMessageInToast(getString(R.string.text_note_added));
                    returnResult(Constants.RESULT_CODE_ADD_NOTE);
                }
                break;

            case PUT_UPDATE_NOTE:
                UpdateNoteResultModel updateNoteResultModel = (UpdateNoteResultModel) baseResultModel;
                if (updateNoteResultModel.isUpdated()) {
                    showMessageInToast(getString(R.string.text_note_updated));
                    returnResult(Constants.RESULT_CODE_UPDATE_NOTE);
                }
                break;

            case DELETE_DELETE_NOTE:
                DeleteNoteResultModel deleteNoteResultModel = (DeleteNoteResultModel) baseResultModel;
                if (deleteNoteResultModel.isDeleted()) {
                    showMessageInToast(getString(R.string.text_note_deleted));
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
            showMessageInSnackbar(editTextMessage, message);
        }
    }

    @Override
    public void onDialogPositiveClicked() {
        Log.d(TAG, "onDialogPositiveClicked()");
        deleteNote();
    }

    @Override
    public void onDialogNegativeClicked() {
        Log.d(TAG, "onDialogNegativeClicked()");
        showSoftKeyboard();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState()");
        outState.putSerializable(Constants.KEY_NOTE, new NoteModel(datetime, editTextMessage.getText().toString()));
    }

    private void showDeleteNoteDialogFragment() {
        Log.d(TAG, "showDeleteNoteDialogFragment()");
        DeleteNoteDialogFragment deleteNoteDialogFragmentFrag =
                new DeleteNoteDialogFragment();
        deleteNoteDialogFragmentFrag.show(getSupportFragmentManager(), Constants.TAG_OF_DELETE_NOTE_DIALOG_FRAGMENT);
    }

    private void returnResult(int resultCode) {
        Intent intent = new Intent();
        intent.putExtra(Constants.KEY_NOTE, note);
        intent.putExtra(Constants.KEY_NOTE_POSITION, notePosition);
        setResult(resultCode, intent);
        finish();
    }

    private void fillViews() {
        if (note != null) {
            datetime = note.getDatetime();
            editTextMessage.setText(note.getMessage());
            editTextMessage.setSelection(note.getMessage().length());
        } else {
            datetime = Calendar.getInstance().getTimeInMillis();
        }
        setActionBarTitle(Util.getStringDatetime(datetime));
    }

    private void showMessageInToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void saveNote(String message) {
        Bundle bundle = new Bundle();
        progressBarActionsWithNote.setVisibility(View.VISIBLE);
        bundle.putSerializable(Constants.KEY_REQUEST, getRequest(message));
        getSupportLoaderManager().restartLoader(Constants.LOADER_ID_API_SERVICE, bundle, this);
    }

    private void deleteNote() {
        Log.d(TAG, "deleteNote()");
        Bundle bundle = new Bundle();
        if (note != null) {
            progressBarActionsWithNote.setVisibility(View.VISIBLE);
            bundle.putSerializable(Constants.KEY_REQUEST, new DeleteNoteRequest(new DeleteNoteRequestModel(note
                    .getDatetime())));
            getSupportLoaderManager().restartLoader(Constants.LOADER_ID_API_SERVICE, bundle, this);
        }
    }

    @NonNull
    private BaseRequest getRequest(String message) {
        if (notePosition == -1) {
            note = new NoteModel(datetime, message);
            return new AddNoteRequest(new AddNoteRequestModel(note));
        } else {
            note.setMessage(message);
            return new UpdateNoteRequest(new UpdateNoteRequestModel(note));
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
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initViews() {
        editTextMessage = findViewById(R.id.editTextMessage);
        editTextMessage.requestFocus();
        progressBarActionsWithNote = findViewById(R.id.progressBarActionsWithNote);
    }
}