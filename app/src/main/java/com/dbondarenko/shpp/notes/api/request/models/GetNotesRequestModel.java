package com.dbondarenko.shpp.notes.api.request.models;

import android.os.Parcel;

import com.dbondarenko.shpp.notes.api.request.models.base.BaseRequestModel;

/**
 * File: GetNotesRequestModel.java
 *
 * @author Dmytro Bondarenko
 *         Date: 21.02.2018
 *         Time: 9:36
 *         E-mail: bondes87@gmail.com
 */
public class GetNotesRequestModel extends BaseRequestModel {

    private int startPosition;
    private int amount;

    public GetNotesRequestModel(int startPosition, int amount) {
        this.startPosition = startPosition;
        this.amount = amount;
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