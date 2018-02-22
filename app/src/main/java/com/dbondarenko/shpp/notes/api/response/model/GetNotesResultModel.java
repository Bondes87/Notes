package com.dbondarenko.shpp.notes.api.response.model;

import com.dbondarenko.shpp.notes.api.response.model.base.BaseResultModel;
import com.dbondarenko.shpp.notes.models.NoteModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * File: GetNotesResultModel.java
 *
 * @author Dmytro Bondarenko
 *         Date: 21.02.2018
 *         Time: 10:10
 *         E-mail: bondes87@gmail.com
 */
public class GetNotesResultModel extends BaseResultModel {

    @SerializedName("notes")
    @Expose
    private List<NoteModel> notes;

    public GetNotesResultModel() {
    }

    public GetNotesResultModel(List<NoteModel> notes) {
        this.notes = notes;
    }

    public List<NoteModel> getNotes() {
        return notes;
    }
}