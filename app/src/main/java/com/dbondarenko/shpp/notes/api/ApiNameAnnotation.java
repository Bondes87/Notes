package com.dbondarenko.shpp.notes.api;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * File: ApiNameAnnotation.java
 *
 * @author Dmytro Bondarenko
 *         Date: 20.02.2018
 *         Time: 12:09
 *         E-mail: bondes87@gmail.com
 */
public class ApiNameAnnotation {

    public static final String POST_ADD_NOTE = "POST_ADD_NOTE";
    public static final String DELETE_DELETE_NOTE = "DELETE_DELETE_NOTE";
    public static final String DELETE_DELETE_NOTES = "DELETE_DELETE_NOTES";
    public static final String GET_GET_NOTES = "GET_GET_NOTES";
    public static final String PUT_UPDATE_NOTE = "PUT_UPDATE_NOTE";
    private final String apiName;

    public ApiNameAnnotation(@ApiName String apiName) {
        this.apiName = apiName;
    }

    public String getApiName() {
        return apiName;
    }

    @StringDef({POST_ADD_NOTE, DELETE_DELETE_NOTE,
            DELETE_DELETE_NOTES, GET_GET_NOTES, PUT_UPDATE_NOTE})
    @Retention(RetentionPolicy.SOURCE)
    @interface ApiName {
    }
}