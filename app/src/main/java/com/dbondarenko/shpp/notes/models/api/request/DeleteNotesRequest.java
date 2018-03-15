package com.dbondarenko.shpp.notes.models.api.request;

import com.dbondarenko.shpp.notes.api.ApiName;
import com.dbondarenko.shpp.notes.models.api.request.base.BaseRequest;
import com.dbondarenko.shpp.notes.models.api.request.models.DeleteNotesRequestModel;

/**
 * File: DeleteNotesRequest.java
 *
 * @author Dmytro Bondarenko
 *         Date: 27.02.2018
 *         Time: 22:30
 *         E-mail: bondes87@gmail.com
 */
public class DeleteNotesRequest extends BaseRequest<DeleteNotesRequestModel> {
    public DeleteNotesRequest(DeleteNotesRequestModel requestModel) {
        super(ApiName.DELETE_DELETE_NOTES, requestModel);
    }
}