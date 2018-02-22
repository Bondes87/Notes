package com.dbondarenko.shpp.notes.ui.loaders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.dbondarenko.shpp.notes.api.ApiLoaderResponse;
import com.dbondarenko.shpp.notes.api.ApiService;
import com.dbondarenko.shpp.notes.api.RetrofitHelper;
import com.dbondarenko.shpp.notes.api.request.GetNotesRequest;
import com.dbondarenko.shpp.notes.api.request.base.BaseRequest;
import com.dbondarenko.shpp.notes.api.response.GetNotesResponse;

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
        if (baseRequest != null && baseRequest.getApiName() != null && apiService != null) {
            switch (baseRequest.getApiName()) {

                case GET_GET_NOTES:
                    return getApiLoaderGetNotesResponse(apiService);

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

    @NonNull
    private ApiLoaderResponse getApiLoaderGetNotesResponse(ApiService apiService) {
        Log.d(TAG, "getApiLoaderGetNotesResponse() " + apiService);
        ApiLoaderResponse<GetNotesResponse> apiLoaderGetNotesResponse = new ApiLoaderResponse<>();
        apiLoaderGetNotesResponse.setApiName(baseRequest.getApiName());
        GetNotesRequest getNotesRequest = (GetNotesRequest) baseRequest;
        int startPosition = getNotesRequest.getRequestModel().getStartPosition();
        int amount = getNotesRequest.getRequestModel().getAmount();
        try {
            apiLoaderGetNotesResponse.setResponse(apiService.getNotes(startPosition, amount).execute());
        } catch (Exception e) {
            e.printStackTrace();
            apiLoaderGetNotesResponse.setException(e);
        }
        return apiLoaderGetNotesResponse;
    }
}