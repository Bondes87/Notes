package com.dbondarenko.shpp.notes.api.request.models;

import android.os.Parcel;

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

    public static final Creator<AddNoteRequestModel> CREATOR =
            new Creator<AddNoteRequestModel>() {
                @Override
                public AddNoteRequestModel createFromParcel(Parcel source) {
                    return new AddNoteRequestModel(source);
                }

                @Override
                public AddNoteRequestModel[] newArray(int size) {
                    return new AddNoteRequestModel[size];
                }
            };

    private NoteModel note;

    public AddNoteRequestModel(NoteModel note) {
        this.note = note;
    }

    protected AddNoteRequestModel(Parcel in) {
        this.note = in.readParcelable(NoteModel.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.note, flags);
    }

    public NoteModel getNote() {
        return note;
    }

    public void setNote(NoteModel note) {
        this.note = note;
    }
}