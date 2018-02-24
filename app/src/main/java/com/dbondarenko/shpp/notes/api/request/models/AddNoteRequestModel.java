package com.dbondarenko.shpp.notes.api.request.models;

import com.dbondarenko.shpp.notes.api.request.models.base.BaseRequestModel;
import com.dbondarenko.shpp.notes.models.NoteModel;

/**
 * File: AddNoteRequestModel.java
 *
 * @author Dmytro Bondarenko
 *         Date: 23.02.2018
 *         Time: 12:58
 *         E-mail: bondes87@gmail.com
 */
public class AddNoteRequestModel extends BaseRequestModel {

    private NoteModel note;

    public AddNoteRequestModel(NoteModel note) {
        this.note = note;
    }

    public NoteModel getNote() {
        return note;
    }

    public void setNote(NoteModel note) {
        this.note = note;
    }
}