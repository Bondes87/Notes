package com.dbondarenko.shpp.notes.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * File: NoteModel.java
 *
 * @author Dmytro Bondarenko
 *         Date: 19.02.2018
 *         Time: 18:41
 *         E-mail: bondes87@gmail.com
 */
public class NoteModel implements Serializable {

    @SerializedName("datetime")
    @Expose
    private long datetime;

    @SerializedName("message")
    @Expose
    private String message;

    public NoteModel() {
    }

    public NoteModel(long datetime, String message) {
        this.datetime = datetime;
        this.message = message;
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