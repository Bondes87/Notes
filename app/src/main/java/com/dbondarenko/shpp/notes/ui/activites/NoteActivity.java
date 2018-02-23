package com.dbondarenko.shpp.notes.ui.activites;

import com.dbondarenko.shpp.notes.R;
import com.dbondarenko.shpp.notes.api.ApiName;
import com.dbondarenko.shpp.notes.api.response.model.base.BaseErrorModel;
import com.dbondarenko.shpp.notes.api.response.model.base.BaseResultModel;
import com.dbondarenko.shpp.notes.ui.activites.base.BaseActivity;

public class NoteActivity extends BaseActivity {

    @Override
    public int getContentViewResourceId() {
        return R.layout.activity_note;
    }

    @Override
    public void handleSuccessResult(ApiName apiName, BaseResultModel baseResultModel) {

    }

    @Override
    public void handleFailureResult(BaseErrorModel baseErrorModel) {

    }
}