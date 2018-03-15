package com.dbondarenko.shpp.notes.models.api.request.base;

import com.dbondarenko.shpp.notes.api.ApiName;
import com.dbondarenko.shpp.notes.api.ApiNameAnnotation;

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
    private ApiNameAnnotation apiNameAnnotation;
    private T requestModel;

    public BaseRequest(ApiNameAnnotation apiNameAnnotation, T requestModel) {
        this.apiNameAnnotation = apiNameAnnotation;
        this.requestModel = requestModel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseRequest<?> that = (BaseRequest<?>) o;
        return apiNameAnnotation == that.apiNameAnnotation && requestModel.equals(that.requestModel);
    }

    @Override
    public int hashCode() {
        int result = apiNameAnnotation.hashCode();
        result = 31 * result + requestModel.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "BaseRequest{" +
                "apiNameAnnotation=" + apiNameAnnotation +
                ", requestModel=" + requestModel +
                '}';
    }

    public ApiNameAnnotation getApiNameAnnotation() {
        return apiNameAnnotation;
    }

    public void setApiNameAnnotation(ApiNameAnnotation apiNameAnnotation) {
        this.apiNameAnnotation = apiNameAnnotation;
    }

    public T getRequestModel() {
        return requestModel;
    }

    public void setRequestModel(T requestModel) {
        this.requestModel = requestModel;
    }
}