package com.dbondarenko.shpp.core.models.responses.models;

import com.dbondarenko.shpp.core.models.NoteModel;
import com.dbondarenko.shpp.core.models.responses.models.base.BaseResultModel;
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

    @SerializedName("totalAmountOfNotesOnServer")
    @Expose
    private int totalAmountOfNotesOnServer;

    public GetNotesResultModel() {
    }

    public GetNotesResultModel(List<NoteModel> notes, int totalAmountOfNotesOnServer) {
        this.notes = notes;
        this.totalAmountOfNotesOnServer = totalAmountOfNotesOnServer;
    }

    public List<NoteModel> getNotes() {
        return notes;
    }

    public int getTotalAmountOfNotesOnServer() {
        return totalAmountOfNotesOnServer;
    }
}