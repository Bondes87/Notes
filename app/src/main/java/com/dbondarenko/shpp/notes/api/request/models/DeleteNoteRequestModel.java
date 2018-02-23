package com.dbondarenko.shpp.notes.api.request.models;

import android.os.Parcel;

import com.dbondarenko.shpp.notes.api.request.models.base.BaseRequestModel;

/**
 * File: DeleteNoteRequestModel.java
 *
 * @author Dmytro Bondarenko
 *         Date: 23.02.2018
 *         Time: 20:22
 *         E-mail: bondes87@gmail.com
 */
public class DeleteNoteRequestModel extends BaseRequestModel {

    public static final Creator<DeleteNoteRequestModel> CREATOR =
            new Creator<DeleteNoteRequestModel>() {
                @Override
                public DeleteNoteRequestModel createFromParcel(Parcel source) {
                    return new DeleteNoteRequestModel(source);
                }

                @Override
                public DeleteNoteRequestModel[] newArray(int size) {
                    return new DeleteNoteRequestModel[size];
                }
            };

    private long datetime;

    public DeleteNoteRequestModel(long datetime) {
        this.datetime = datetime;
    }

    protected DeleteNoteRequestModel(Parcel in) {
        this.datetime = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.datetime);
    }

    public long getDatetime() {
        return datetime;
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }
}