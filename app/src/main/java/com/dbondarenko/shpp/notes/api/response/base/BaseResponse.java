package com.dbondarenko.shpp.notes.api.response.base;

import com.dbondarenko.shpp.notes.api.response.model.base.BaseErrorModel;
import com.dbondarenko.shpp.notes.api.response.model.base.BaseResultModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * File: BaseResponse.java
 *
 * @author Dmytro Bondarenko
 *         Date: 21.02.2018
 *         Time: 19:53
 *         E-mail: bondes87@gmail.com
 */
public abstract class BaseResponse<T extends BaseResultModel> {

    @SerializedName("error")
    @Expose
    private BaseErrorModel error;
    @SerializedName("result")
    @Expose
    private T result;

    public BaseResponse() {
    }

    public BaseResponse(T result) {
        this.result = result;
    }

    public BaseResponse(BaseErrorModel error) {
        this.error = error;
    }

    public BaseResponse(BaseErrorModel error, T result) {
        this.error = error;
        this.result = result;
    }

    public BaseErrorModel getError() {
        return error;
    }

    public BaseResultModel getResult() {
        return result;
    }
}