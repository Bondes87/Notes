package com.dbondarenko.shpp.notes.api.request.models;

import com.dbondarenko.shpp.notes.api.request.models.base.BaseRequestModel;

/**
 * File: DeleteNotesRequestModel.java
 *
 * @author Dmytro Bondarenko
 *         Date: 27.02.2018
 *         Time: 22:25
 *         E-mail: bondes87@gmail.com
 */
public class DeleteNotesRequestModel extends BaseRequestModel {
    private long[] datetimeArray;

    public DeleteNotesRequestModel(long[] datetimeArray) {
        this.datetimeArray = datetimeArray;
    }

    public long[] getDatetimeArray() {
        return datetimeArray;
    }

    public void setDatetimeArray(long[] datetimeArray) {
        this.datetimeArray = datetimeArray;
    }
}