package com.dbondarenko.shpp.notes.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dbondarenko.shpp.notes.Constants;
import com.dbondarenko.shpp.notes.R;
import com.dbondarenko.shpp.notes.api.ApiLoaderResponse;
import com.dbondarenko.shpp.notes.api.ApiName;
import com.dbondarenko.shpp.notes.api.request.GetNotesRequest;
import com.dbondarenko.shpp.notes.api.request.models.GetNotesModel;
import com.dbondarenko.shpp.notes.api.response.model.GetNotesResultModel;
import com.dbondarenko.shpp.notes.api.response.model.base.BaseErrorModel;
import com.dbondarenko.shpp.notes.api.response.model.base.BaseResultModel;
import com.dbondarenko.shpp.notes.ui.adapters.NoteAdapter;
import com.dbondarenko.shpp.notes.ui.fragments.base.BaseFragment;
import com.dbondarenko.shpp.notes.ui.listeners.OnEmptyListListener;
import com.dbondarenko.shpp.notes.ui.listeners.OnListItemClickListener;
import com.dbondarenko.shpp.notes.ui.loaders.ApiServiceAsyncTaskLoader;

public class NotesListFragment extends BaseFragment implements View.OnClickListener,
        OnEmptyListListener, OnListItemClickListener, LoaderManager.LoaderCallbacks<ApiLoaderResponse> {

    private FloatingActionButton floatingActionButtonAddNote;
    private RecyclerView recyclerViewNotesList;
    private ProgressBar progressBarNotesLoading;
    private TextView textViewNoNotes;

    private NoteAdapter noteAdapter;

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
        bundle.putParcelable(Constants.KEY_NOTE, noteAdapter.getNote(position));
        bundle.putInt(Constants.KEY_NOTE_POSITION, position);
        noteFragment.setArguments(bundle);
        showNoteFragment(noteFragment);
    }

    @Override
    public Loader<ApiLoaderResponse> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader() " + id);
        switch (id) {
            case Constants.LOADER_ID_API_SERVICE:
                if (args != null) {
                    return new ApiServiceAsyncTaskLoader(getContext().getApplicationContext(),
                            new GetNotesRequest(new GetNotesModel(
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
                    showMessage(getView(), getString(R.string.error_loading_data));
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
            showMessage(getView(), message);
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
                break;
        }
    }

    private void initViews(View view) {
        Log.d(TAG, "initViews()");
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
        Log.d(TAG, "initRecyclerViewAdapter()");
        if (noteAdapter == null) {
            noteAdapter = new NoteAdapter(
                    NotesListFragment.this,
                    NotesListFragment.this);
            progressBarNotesLoading.setVisibility(View.VISIBLE);
            initLoader();
        }
    }

    private void initLoader() {
        Log.d(TAG, "initLoader()");
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.KEY_START_NOTE_POSITION, noteAdapter.getItemCount());
        getBaseActivity().getSupportLoaderManager().initLoader(Constants.LOADER_ID_API_SERVICE, bundle, this);
    }
}