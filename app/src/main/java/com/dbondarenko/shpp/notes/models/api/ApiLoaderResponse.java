package com.dbondarenko.shpp.notes.models.api;

import com.dbondarenko.shpp.notes.api.ApiName;
import com.dbondarenko.shpp.notes.models.api.response.base.BaseResponse;

import java.io.Serializable;

import retrofit2.Response;

/**
 * File: ApiLoaderResponse.java
 *
 * @author Dmytro Bondarenko
 *         Date: 21.02.2018
 *         Time: 10:02
 *         E-mail: bondes87@gmail.com
 */
public class ApiLoaderResponse<T extends BaseResponse> implements Serializable {
    private ApiName apiName;
    private Response<T> baseResponseModelResponse;
    private Exception exception;

    public ApiName getApiName() {
        return apiName;
    }

    public void setApiName(ApiName apiName) {
        this.apiName = apiName;
    }

    public T getResponseModel() {
        return baseResponseModelResponse != null ? baseResponseModelResponse.body() : null;
    }

    public void setResponse(Response<T> response) {
        this.baseResponseModelResponse = response;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception e) {
        this.exception = e;
    }
}
