package com.dbondarenko.shpp.core.models.responses.models;

import com.dbondarenko.shpp.core.models.responses.models.base.BaseResultModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * File: DeleteNoteResultModel.java
 *
 * @author Dmytro Bondarenko
 *         Date: 23.02.2018
 *         Time: 20:31
 *         E-mail: bondes87@gmail.com
 */
public class DeleteNoteResultModel extends BaseResultModel {

    @SerializedName("deleted")
    @Expose
    private boolean isDeleted;

    public DeleteNoteResultModel(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public boolean isDeleted() {
        return isDeleted;
    }
}