package com.dbondarenko.shpp.core.models.responses;

import com.dbondarenko.shpp.core.models.responses.models.base.BaseErrorModel;
import com.dbondarenko.shpp.core.models.responses.models.base.BaseResultModel;

/**
 * File: ApiResponse.java
 *
 * @author Dmytro Bondarenko
 *         Date: 05.02.2018
 *         Time: 13:33
 *         E-mail: bondes87@gmail.com
 */
public class ApiResponse {
    private BaseErrorModel error;
    private BaseResultModel result;

    public ApiResponse(BaseResultModel result) {
        this.result = result;
    }

    public ApiResponse(BaseErrorModel error) {
        this.error = error;
    }

    public ApiResponse(BaseErrorModel error, BaseResultModel result) {
        this.error = error;
        this.result = result;
    }

    public BaseErrorModel getError() {
        return error;
    }

    public BaseResultModel getResult() {
        return result;
    }
}