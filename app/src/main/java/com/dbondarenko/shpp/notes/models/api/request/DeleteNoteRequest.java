package com.dbondarenko.shpp.notes.models.api.request;

import com.dbondarenko.shpp.notes.api.ApiNameAnnotation;
import com.dbondarenko.shpp.notes.models.api.request.base.BaseRequest;
import com.dbondarenko.shpp.notes.models.api.request.models.DeleteNoteRequestModel;

/**
 * File: DeleteNoteRequest.java
 *
 * @author Dmytro Bondarenko
 *         Date: 23.02.2018
 *         Time: 20:24
 *         E-mail: bondes87@gmail.com
 */
public class DeleteNoteRequest extends BaseRequest<DeleteNoteRequestModel> {
    public DeleteNoteRequest(DeleteNoteRequestModel requestModel) {
        super(new ApiNameAnnotation(ApiNameAnnotation.DELETE_DELETE_NOTE), requestModel);
    }
}