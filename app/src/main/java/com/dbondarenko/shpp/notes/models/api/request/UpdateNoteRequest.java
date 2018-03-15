package com.dbondarenko.shpp.notes.models.api.request;

import com.dbondarenko.shpp.notes.api.ApiName;
import com.dbondarenko.shpp.notes.models.api.request.base.BaseRequest;
import com.dbondarenko.shpp.notes.models.api.request.models.UpdateNoteRequestModel;

/**
 * File: UpdateNoteRequest.java
 *
 * @author Dmytro Bondarenko
 *         Date: 23.02.2018
 *         Time: 20:18
 *         E-mail: bondes87@gmail.com
 */
public class UpdateNoteRequest extends BaseRequest<UpdateNoteRequestModel> {
    public UpdateNoteRequest(UpdateNoteRequestModel requestModel) {
        super(ApiName.PUT_UPDATE_NOTE, requestModel);
    }
}