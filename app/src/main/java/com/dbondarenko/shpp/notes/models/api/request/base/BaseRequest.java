package com.dbondarenko.shpp.notes.models.api.request.base;

import com.dbondarenko.shpp.notes.api.ApiName;

import java.io.Serializable;

/**
 * File: BaseRequest.java
 *
 * @author Dmytro Bondarenko
 *         Date: 20.02.2018
 *         Time: 15:20
 *         E-mail: bondes87@gmail.com
 */
public abstract class BaseRequest<T> implements Serializable{
    private ApiName apiName;
    private T requestModel;

    public BaseRequest(ApiName apiName, T requestModel) {
        this.apiName = apiName;
        this.requestModel = requestModel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseRequest<?> that = (BaseRequest<?>) o;
        return apiName == that.apiName && requestModel.equals(that.requestModel);
    }

    @Override
    public int hashCode() {
        int result = apiName.hashCode();
        result = 31 * result + requestModel.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "BaseRequest{" +
                "apiName=" + apiName +
                ", requestModel=" + requestModel +
                '}';
    }

    public ApiName getApiName() {
        return apiName;
    }

    public void setApiName(ApiName apiName) {
        this.apiName = apiName;
    }

    public T getRequestModel() {
        return requestModel;
    }

    public void setRequestModel(T requestModel) {
        this.requestModel = requestModel;
    }
}