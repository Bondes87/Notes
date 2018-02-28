package com.dbondarenko.shpp.notes.models.api.request.models;

import com.dbondarenko.shpp.notes.models.api.request.models.base.BaseRequestModel;

/**
 * File: DeleteNoteRequestModel.java
 *
 * @author Dmytro Bondarenko
 *         Date: 23.02.2018
 *         Time: 20:22
 *         E-mail: bondes87@gmail.com
 */
public class DeleteNoteRequestModel extends BaseRequestModel {

    private long datetime;

    public DeleteNoteRequestModel(long datetime) {
        this.datetime = datetime;
    }

    public long getDatetime() {
        return datetime;
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }
}