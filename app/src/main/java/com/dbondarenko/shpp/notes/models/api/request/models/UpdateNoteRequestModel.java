package com.dbondarenko.shpp.notes.models.api.request.models;

import android.os.Parcel;

import com.dbondarenko.shpp.notes.models.api.request.models.base.BaseRequestModel;
import com.dbondarenko.shpp.core.models.NoteModel;

/**
 * File: UpdateNoteRequestModel.java
 *
 * @author Dmytro Bondarenko
 *         Date: 23.02.2018
 *         Time: 20:17
 *         E-mail: bondes87@gmail.com
 */
public class UpdateNoteRequestModel extends BaseRequestModel {

    private NoteModel note;

    public UpdateNoteRequestModel(NoteModel note) {
        this.note = note;
    }

    protected UpdateNoteRequestModel(Parcel in) {
        this.note = in.readParcelable(NoteModel.class.getClassLoader());
    }

    public NoteModel getNote() {
        return note;
    }

    public void setNote(NoteModel note) {
        this.note = note;
    }
}