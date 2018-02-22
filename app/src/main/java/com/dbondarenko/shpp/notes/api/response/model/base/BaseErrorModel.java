package com.dbondarenko.shpp.notes.api.response.model.base;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * File: BaseErrorModel.java
 *
 * @author Dmytro Bondarenko
 *         Date: 22.02.2018
 *         Time: 12:25
 *         E-mail: bondes87@gmail.com
 */
public class BaseErrorModel {
    @SerializedName("message")
    @Expose
    private String message;

    public BaseErrorModel() {
    }

    public BaseErrorModel(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}