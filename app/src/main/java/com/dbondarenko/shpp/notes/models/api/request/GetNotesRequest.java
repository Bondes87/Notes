package com.dbondarenko.shpp.notes.models.api.request;

import com.dbondarenko.shpp.notes.api.ApiNameAnnotation;
import com.dbondarenko.shpp.notes.models.api.request.base.BaseRequest;
import com.dbondarenko.shpp.notes.models.api.request.models.GetNotesRequestModel;

/**
 * File: GetNotesRequest.java
 *
 * @author Dmytro Bondarenko
 *         Date: 21.02.2018
 *         Time: 9:46
 *         E-mail: bondes87@gmail.com
 */
public class GetNotesRequest extends BaseRequest<GetNotesRequestModel> {
    public GetNotesRequest(GetNotesRequestModel requestModel) {
        super(new ApiNameAnnotation(ApiNameAnnotation.GET_GET_NOTES), requestModel);
    }
}