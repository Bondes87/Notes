package com.dbondarenko.shpp.core.models;

/**
 * File: ApiResponse.java
 *
 * @author Dmytro Bondarenko
 *         Date: 05.02.2018
 *         Time: 13:33
 *         E-mail: bondes87@gmail.com
 */
public class ApiResponse {
    private Error mError;
    private Result mResult;

    public ApiResponse(Result result) {
        mResult = result;
    }

    public ApiResponse(Error error) {
        mError = error;
    }

    public ApiResponse(Error error, Result result) {
        mError = error;
        mResult = result;
    }

    public Error getError() {
        return mError;
    }

    public Result getResult() {
        return mResult;
    }
}
