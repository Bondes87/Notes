package com.dbondarenko.shpp.notes.api.request.models;

import android.os.Parcel;

import com.dbondarenko.shpp.notes.api.request.models.base.BaseRequestModel;

/**
 * File: GetNotesModel.java
 *
 * @author Dmytro Bondarenko
 *         Date: 21.02.2018
 *         Time: 9:36
 *         E-mail: bondes87@gmail.com
 */
public class GetNotesModel extends BaseRequestModel {

    public static final Creator<GetNotesModel> CREATOR =
            new Creator<GetNotesModel>() {
                @Override
                public GetNotesModel createFromParcel(Parcel source) {
                    return new GetNotesModel(source);
                }

                @Override
                public GetNotesModel[] newArray(int size) {
                    return new GetNotesModel[size];
                }
            };

    private int startPosition;
    private int amount;

    public GetNotesModel(int startPosition, int amount) {
        this.startPosition = startPosition;
        this.amount = amount;
    }

    protected GetNotesModel(Parcel in) {
        this.startPosition = in.readInt();
        this.amount = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.startPosition);
        dest.writeInt(this.amount);
    }

    public int getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(int startPosition) {
        this.startPosition = startPosition;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}