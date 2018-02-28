package com.dbondarenko.shpp.core.models.responses.models;

import com.dbondarenko.shpp.core.models.responses.models.base.BaseResultModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * File: DeleteNotesResultModel.java
 *
 * @author Dmytro Bondarenko
 *         Date: 27.02.2018
 *         Time: 22:31
 *         E-mail: bondes87@gmail.com
 */
public class DeleteNotesResultModel extends BaseResultModel {
    @SerializedName("deleted")
    @Expose
    private boolean isDeleted;

    public DeleteNotesResultModel(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public boolean isDeleted() {
        return isDeleted;
    }
}