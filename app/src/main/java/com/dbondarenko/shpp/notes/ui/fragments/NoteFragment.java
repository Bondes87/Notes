package com.dbondarenko.shpp.notes.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dbondarenko.shpp.notes.Constants;
import com.dbondarenko.shpp.notes.R;
import com.dbondarenko.shpp.notes.api.ApiLoaderResponse;
import com.dbondarenko.shpp.notes.api.ApiName;
import com.dbondarenko.shpp.notes.api.request.AddNoteRequest;
import com.dbondarenko.shpp.notes.api.request.base.BaseRequest;
import com.dbondarenko.shpp.notes.api.request.models.AddNoteRequestModel;
import com.dbondarenko.shpp.notes.api.response.model.AddNoteResultModel;
import com.dbondarenko.shpp.notes.api.response.model.base.BaseErrorModel;
import com.dbondarenko.shpp.notes.api.response.model.base.BaseResultModel;
import com.dbondarenko.shpp.notes.models.NoteModel;
import com.dbondarenko.shpp.notes.ui.fragments.base.BaseFragment;
import com.dbondarenko.shpp.notes.ui.loaders.ApiServiceAsyncTaskLoader;
import com.dbondarenko.shpp.notes.utils.Util;

import java.util.Calendar;

public class NoteFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<ApiLoaderResponse> {

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(TAG, "onCreateOptionsMenu()");
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_note_menu, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        Log.d(TAG, "onPrepareOptionsMenu()");
        super.onPrepareOptionsMenu(menu);
        if (note == null) {
            menu.findItem(R.id.itemDeleteNote).setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected()");
        String message = editTextMessage.getText().toString();
        Bundle bundle = new Bundle();
        switch (item.getItemId()) {
            case R.id.itemSaveNote:
                if (TextUtils.isEmpty(message)) {
                    showMessageInSnackbar(getView(), getString(R.string.error_note_is_empty));
                    return true;
                }
                hideSoftKeyboard();
                saveNote(message, bundle);
                return true;
            case R.id.itemDeleteNote:
                hideSoftKeyboard();
                // showDeleteNoteDialogFragment();
                return true;
            case android.R.id.home:
                hideSoftKeyboard();
                getFragmentManager().popBackStack();
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
                    return new ApiServiceAsyncTaskLoader(getContext().getApplicationContext(),
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
                    showMessageInSnackbar(getView(), getString(R.string.error_loading_data));
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
                    showMessageInToast(getString(R.string.text_note_saved));
                    getActivity().finish();
                    getFragmentManager().popBackStack();
                }
                break;
        }
    }

    @Override
    public void handleFailureResult(BaseErrorModel baseErrorModel) {
        Log.d(TAG, "handleFailureResult()");
        String message = baseErrorModel.getMessage();
        if (!TextUtils.isEmpty(message)) {
            showMessageInSnackbar(getView(), message);
        }
    }

    private void showMessageInToast(String message) {
        Toast.makeText(getContext().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void saveNote(String message, Bundle bundle) {
        progressBarActionsWithNote.setVisibility(View.VISIBLE);
        bundle.putSerializable(Constants.KEY_REQUEST, getRequest(message));
        getLoaderManager().restartLoader(Constants.LOADER_ID_API_SERVICE, bundle, this);
    }

    @NonNull
    private BaseRequest getRequest(String message) {
        if (note == null) {
            return new AddNoteRequest(new AddNoteRequestModel(
                    new NoteModel(datetime, message)));
        } else {
            return null;
        }
    }


    private void hideSoftKeyboard() {
        Log.d(TAG, "hideSoftKeyboard()");
        InputMethodManager inputMethodManager =
                (InputMethodManager) getContext().getApplicationContext()
                        .getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (getView() != null && inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        }
    }

    private void showSoftKeyboard() {
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