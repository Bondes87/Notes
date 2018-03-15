package com.dbondarenko.shpp.notes.ui.loaders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.dbondarenko.shpp.core.models.NoteModel;
import com.dbondarenko.shpp.notes.R;
import com.dbondarenko.shpp.notes.api.ApiNameAnnotation;
import com.dbondarenko.shpp.notes.api.ApiService;
import com.dbondarenko.shpp.notes.api.RetrofitHelper;
import com.dbondarenko.shpp.notes.models.api.ApiLoaderResponse;
import com.dbondarenko.shpp.notes.models.api.request.AddNoteRequest;
import com.dbondarenko.shpp.notes.models.api.request.DeleteNoteRequest;
import com.dbondarenko.shpp.notes.models.api.request.DeleteNotesRequest;
import com.dbondarenko.shpp.notes.models.api.request.GetNotesRequest;
import com.dbondarenko.shpp.notes.models.api.request.UpdateNoteRequest;
import com.dbondarenko.shpp.notes.models.api.request.base.BaseRequest;
import com.dbondarenko.shpp.notes.models.api.response.AddNoteResponse;
import com.dbondarenko.shpp.notes.models.api.response.DeleteNoteResponse;
import com.dbondarenko.shpp.notes.models.api.response.DeleteNotesResponse;
import com.dbondarenko.shpp.notes.models.api.response.GetNotesResponse;
import com.dbondarenko.shpp.notes.models.api.response.UpdateNoteResponse;
import com.dbondarenko.shpp.notes.models.exception.NoInternetConnectionException;
import com.dbondarenko.shpp.notes.utils.Util;

import retrofit2.Retrofit;

/**
 * File: BaseResponse.java
 *
 * @author Dmytro Bondarenko
 *         Date: 21.02.2018
 *         Time: 10:11
 *         E-mail: bondes87@gmail.com
 */
public class ApiServiceAsyncTaskLoader extends AsyncTaskLoader<ApiLoaderResponse> {

    private static final String TAG = ApiServiceAsyncTaskLoader.class.getSimpleName();

    private BaseRequest baseRequest;

    public ApiServiceAsyncTaskLoader(Context context, BaseRequest baseRequest) {
        super(context);
        Log.d(TAG, "ApiServiceAsyncTaskLoader() " + baseRequest);
        this.baseRequest = baseRequest;
        onContentChanged();
    }

    @Override
    public void onStartLoading() {
        Log.d(TAG, "onStartLoading()");
        if (takeContentChanged())
            forceLoad();
    }

    @Override
    public ApiLoaderResponse loadInBackground() {
        Log.d(TAG, "loadInBackground()");
        Retrofit retrofit = RetrofitHelper.getInstance().getRetrofit();
        ApiService apiService = retrofit.create(ApiService.class);
        if (baseRequest != null && baseRequest.getApiNameAnnotation() != null && apiService != null) {
            switch (baseRequest.getApiNameAnnotation().getApiName()) {

                case ApiNameAnnotation.GET_GET_NOTES:
                    return getApiLoaderGetNotesResponse(apiService);

                case ApiNameAnnotation.POST_ADD_NOTE:
                    return getApiLoaderAddNoteResponse(apiService);

                case ApiNameAnnotation.PUT_UPDATE_NOTE:
                    return gerApiLoaderUpdateNoteResponse(apiService);

                case ApiNameAnnotation.DELETE_DELETE_NOTE:
                    return gerApiLoaderDeleteNoteResponse(apiService);

                case ApiNameAnnotation.DELETE_DELETE_NOTES:
                    return gerApiLoaderDeleteNotesResponse(apiService);

                default:
                    return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public void onStopLoading() {
        Log.d(TAG, "onStopLoading()");
        cancelLoad();
    }

    private ApiLoaderResponse gerApiLoaderDeleteNotesResponse(ApiService apiService) {
        Log.d(TAG, "gerApiLoaderDeleteNoteResponse() " + apiService);
        ApiLoaderResponse<DeleteNotesResponse> apiLoaderDeleteNotesResponse = new ApiLoaderResponse<>();
        apiLoaderDeleteNotesResponse.setApiNameAnnotation(baseRequest.getApiNameAnnotation());
        DeleteNotesRequest deleteNotesRequest = (DeleteNotesRequest) baseRequest;
        long[] datetimeArray = deleteNotesRequest.getRequestModel().getDatetimeArray();
        if (Util.isInternetConnectionAvailable(getContext().getApplicationContext())) {
            try {
                apiLoaderDeleteNotesResponse.setResponse(apiService.deleteNotes(datetimeArray).execute());
            } catch (Exception e) {
                e.printStackTrace();
                apiLoaderDeleteNotesResponse.setException(e);
            }
        } else {
            apiLoaderDeleteNotesResponse.setException(new NoInternetConnectionException(
                    getContext().getApplicationContext().getString(R.string.error_no_connection)));
        }
        return apiLoaderDeleteNotesResponse;
    }


    private ApiLoaderResponse gerApiLoaderDeleteNoteResponse(ApiService apiService) {
        Log.d(TAG, "gerApiLoaderDeleteNoteResponse() " + apiService);
        ApiLoaderResponse<DeleteNoteResponse> apiLoaderDeleteNoteResponse = new ApiLoaderResponse<>();
        apiLoaderDeleteNoteResponse.setApiNameAnnotation(baseRequest.getApiNameAnnotation());
        DeleteNoteRequest deleteNoteRequest = (DeleteNoteRequest) baseRequest;
        long datetime = deleteNoteRequest.getRequestModel().getDatetime();
        if (Util.isInternetConnectionAvailable(getContext().getApplicationContext())) {
            try {
                apiLoaderDeleteNoteResponse.setResponse(apiService.deleteNote(datetime).execute());
            } catch (Exception e) {
                e.printStackTrace();
                apiLoaderDeleteNoteResponse.setException(e);
            }
        } else {
            apiLoaderDeleteNoteResponse.setException(new NoInternetConnectionException(
                    getContext().getApplicationContext().getString(R.string.error_no_connection)));
        }
        return apiLoaderDeleteNoteResponse;
    }

    private ApiLoaderResponse gerApiLoaderUpdateNoteResponse(ApiService apiService) {
        Log.d(TAG, "gerApiLoaderUpdateNoteResponse() " + apiService);
        ApiLoaderResponse<UpdateNoteResponse> apiLoaderUpdateNoteResponse = new ApiLoaderResponse<>();
        apiLoaderUpdateNoteResponse.setApiNameAnnotation(baseRequest.getApiNameAnnotation());
        UpdateNoteRequest updateNoteRequest = (UpdateNoteRequest) baseRequest;
        NoteModel note = updateNoteRequest.getRequestModel().getNote();
        if (Util.isInternetConnectionAvailable(getContext().getApplicationContext())) {
            try {
                apiLoaderUpdateNoteResponse.setResponse(apiService.updateNote(note).execute());
            } catch (Exception e) {
                e.printStackTrace();
                apiLoaderUpdateNoteResponse.setException(e);
            }
        } else {
            apiLoaderUpdateNoteResponse.setException(new NoInternetConnectionException(
                    getContext().getApplicationContext().getString(R.string.error_no_connection)));
        }
        return apiLoaderUpdateNoteResponse;
    }

    private ApiLoaderResponse getApiLoaderAddNoteResponse(ApiService apiService) {
        Log.d(TAG, "getApiLoaderAddNoteResponse() " + apiService);
        ApiLoaderResponse<AddNoteResponse> apiLoaderAddNoteResponse = new ApiLoaderResponse<>();
        apiLoaderAddNoteResponse.setApiNameAnnotation(baseRequest.getApiNameAnnotation());
        AddNoteRequest addNoteRequest = (AddNoteRequest) baseRequest;
        NoteModel note = addNoteRequest.getRequestModel().getNote();
        if (Util.isInternetConnectionAvailable(getContext().getApplicationContext())) {
            try {
                apiLoaderAddNoteResponse.setResponse(apiService.addNote(note).execute());
            } catch (Exception e) {
                e.printStackTrace();
                apiLoaderAddNoteResponse.setException(e);
            }
        } else {
            apiLoaderAddNoteResponse.setException(new NoInternetConnectionException(
                    getContext().getApplicationContext().getString(R.string.error_no_connection)));
        }
        return apiLoaderAddNoteResponse;
    }

    @NonNull
    private ApiLoaderResponse getApiLoaderGetNotesResponse(ApiService apiService) {
        Log.d(TAG, "getApiLoaderGetNotesResponse() " + apiService);
        ApiLoaderResponse<GetNotesResponse> apiLoaderGetNotesResponse = new ApiLoaderResponse<>();
        apiLoaderGetNotesResponse.setApiNameAnnotation(baseRequest.getApiNameAnnotation());
        GetNotesRequest getNotesRequest = (GetNotesRequest) baseRequest;
        int startPosition = getNotesRequest.getRequestModel().getStartPosition();
        int amount = getNotesRequest.getRequestModel().getAmount();
        if (Util.isInternetConnectionAvailable(getContext().getApplicationContext())) {
            try {
                apiLoaderGetNotesResponse.setResponse(apiService.getNotes(startPosition, amount).execute());
            } catch (Exception e) {
                e.printStackTrace();
                apiLoaderGetNotesResponse.setException(e);
            }
        } else {
            apiLoaderGetNotesResponse.setException(new NoInternetConnectionException(
                    getContext().getApplicationContext().getString(R.string.error_no_connection)));
        }
        return apiLoaderGetNotesResponse;
    }
}