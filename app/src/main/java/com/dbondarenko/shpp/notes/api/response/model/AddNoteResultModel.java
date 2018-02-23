package com.dbondarenko.shpp.notes.api.response.model;

import com.dbondarenko.shpp.notes.api.response.model.base.BaseResultModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * File: AddNoteResultModel.java
 *
 * @author Dmytro Bondarenko
 *         Date: 23.02.2018
 *         Time: 13:24
 *         E-mail: bondes87@gmail.com
 */
public class AddNoteResultModel extends BaseResultModel {

    @SerializedName("added")
    @Expose
    private boolean isAdded;

    public AddNoteResultModel(boolean isAdded) {
        this.isAdded = isAdded;
    }

    public boolean isAdded() {
        return isAdded;
    }
}