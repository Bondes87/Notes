package com.dbondarenko.shpp.notes.models.api.request;

import com.dbondarenko.shpp.notes.api.ApiNameAnnotation;
import com.dbondarenko.shpp.notes.models.api.request.base.BaseRequest;
import com.dbondarenko.shpp.notes.models.api.request.models.AddNoteRequestModel;

/**
 * File: AddNoteRequest.java
 *
 * @author Dmytro Bondarenko
 *         Date: 23.02.2018
 *         Time: 13:05
 *         E-mail: bondes87@gmail.com
 */
public class AddNoteRequest extends BaseRequest<AddNoteRequestModel> {
    public AddNoteRequest(AddNoteRequestModel requestModel) {
        super(new ApiNameAnnotation(ApiNameAnnotation.POST_ADD_NOTE), requestModel);
    }
}