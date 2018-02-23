package com.dbondarenko.shpp.notes.api.request.models;

import android.os.Parcel;

import com.dbondarenko.shpp.notes.api.request.models.base.BaseRequestModel;
import com.dbondarenko.shpp.notes.models.NoteModel;

/**
 * File: UpdateNoteRequestModel.java
 *
 * @author Dmytro Bondarenko
 *         Date: 23.02.2018
 *         Time: 20:17
 *         E-mail: bondes87@gmail.com
 */
public class UpdateNoteRequestModel extends BaseRequestModel {

    public static final Creator<UpdateNoteRequestModel> CREATOR =
            new Creator<UpdateNoteRequestModel>() {
                @Override
                public UpdateNoteRequestModel createFromParcel(Parcel source) {
                    return new UpdateNoteRequestModel(source);
                }

                @Override
                public UpdateNoteRequestModel[] newArray(int size) {
                    return new UpdateNoteRequestModel[size];
                }
            };

    private NoteModel note;

    public UpdateNoteRequestModel(NoteModel note) {
        this.note = note;
    }

    protected UpdateNoteRequestModel(Parcel in) {
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