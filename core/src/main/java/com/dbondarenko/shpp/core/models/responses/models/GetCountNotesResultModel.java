package com.dbondarenko.shpp.core.models.responses.models;

import com.dbondarenko.shpp.core.models.responses.models.base.BaseResultModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * File: GetCountNotesResultModel.java
 *
 * @author Dmytro Bondarenko
 *         Date: 28.02.2018
 *         Time: 21:44
 *         E-mail: bondes87@gmail.com
 */
public class GetCountNotesResultModel extends BaseResultModel {
    @SerializedName("count")
    @Expose
    private int count;

    public GetCountNotesResultModel(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }
}