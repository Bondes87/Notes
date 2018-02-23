package com.dbondarenko.shpp.notes.api.response.model;

import com.dbondarenko.shpp.notes.api.response.model.base.BaseResultModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * File: UpdateNoteResultModel.java
 *
 * @author Dmytro Bondarenko
 *         Date: 23.02.2018
 *         Time: 20:26
 *         E-mail: bondes87@gmail.com
 */
public class UpdateNoteResultModel extends BaseResultModel{

    @SerializedName("updated")
    @Expose
    private boolean isUpdated;

    public UpdateNoteResultModel(boolean isUpdated) {
        this.isUpdated = isUpdated;
    }

    public boolean isUpdated() {
        return isUpdated;
    }
}