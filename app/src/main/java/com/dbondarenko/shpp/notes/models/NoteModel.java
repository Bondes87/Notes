package com.dbondarenko.shpp.notes.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * File: NoteModel.java
 *
 * @author Dmytro Bondarenko
 *         Date: 19.02.2018
 *         Time: 18:41
 *         E-mail: bondes87@gmail.com
 */
public class NoteModel implements Parcelable {

    public static final Parcelable.Creator<NoteModel> CREATOR =
            new Parcelable.Creator<NoteModel>() {
                @Override
                public NoteModel createFromParcel(Parcel source) {
                    return new NoteModel(source);
                }

                @Override
                public NoteModel[] newArray(int size) {
                    return new NoteModel[size];
                }
            };

    private long datetime;
    private String message;

    public NoteModel(long datetime, String message) {
        this.datetime = datetime;
        this.message = message;
    }

    protected NoteModel(Parcel in) {
        this.datetime = in.readLong();
        this.message = in.readString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NoteModel noteModel = (NoteModel) o;
        return datetime == noteModel.datetime && message.equals(noteModel.message);
    }

    @Override
    public int hashCode() {
        int result = (int) (datetime ^ (datetime >>> 32));
        result = 31 * result + message.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "NoteModel{" +
                "datetime=" + datetime +
                ", message='" + message + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.datetime);
        dest.writeString(this.message);
    }

    public long getDatetime() {
        return datetime;
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}