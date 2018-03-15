package com.dbondarenko.shpp.notes.api;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * File: ApiName.java
 *
 * @author Dmytro Bondarenko
 *         Date: 15.03.2018
 *         Time: 23:00
 *         E-mail: bondes87@gmail.com
 */
@StringDef({ApiName.POST_ADD_NOTE, ApiName.DELETE_DELETE_NOTE,
        ApiName.DELETE_DELETE_NOTES, ApiName.GET_GET_NOTES,
        ApiName.PUT_UPDATE_NOTE})
@Retention(RetentionPolicy.SOURCE)
public @interface ApiName {
    String POST_ADD_NOTE = "POST_ADD_NOTE";
    String DELETE_DELETE_NOTE = "DELETE_DELETE_NOTE";
    String DELETE_DELETE_NOTES = "DELETE_DELETE_NOTES";
    String GET_GET_NOTES = "GET_GET_NOTES";
    String PUT_UPDATE_NOTE = "PUT_UPDATE_NOTE";
}