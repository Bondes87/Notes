package com.dbondarenko.shpp.notes.api.request;

import com.dbondarenko.shpp.notes.api.ApiName;
import com.dbondarenko.shpp.notes.api.request.base.BaseRequest;
import com.dbondarenko.shpp.notes.api.request.models.GetNotesModel;

/**
 * File: GetNotesRequest.java
 *
 * @author Dmytro Bondarenko
 *         Date: 21.02.2018
 *         Time: 9:46
 *         E-mail: bondes87@gmail.com
 */
public class GetNotesRequest extends BaseRequest<GetNotesModel> {
    public GetNotesRequest(GetNotesModel requestModel) {
        super(ApiName.GET_GET_NOTES, requestModel);
    }
}
