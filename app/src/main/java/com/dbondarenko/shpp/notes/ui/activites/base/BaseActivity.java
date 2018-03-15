package com.dbondarenko.shpp.notes.ui.activites.base;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dbondarenko.shpp.core.models.responses.models.base.BaseErrorModel;
import com.dbondarenko.shpp.core.models.responses.models.base.BaseResultModel;
import com.dbondarenko.shpp.notes.api.ApiName;
import com.dbondarenko.shpp.notes.models.api.request.base.BaseRequest;

/**
 * File: BaseActivity.java
 *
 * @author Dmytro Bondarenko
 *         Date: 19.02.2018
 *         Time: 16:16
 *         E-mail: bondes87@gmail.com
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected final String TAG;

    private BaseRequest baseRequest;
    private Snackbar snackbar;

    public BaseActivity() {
        TAG = getClass().getSimpleName();
    }

    public abstract int getContentViewResourceId();

    public abstract void handleSuccessResult(@ApiName String apiName, BaseResultModel baseResultModel);

    public abstract void handleFailureResult(BaseErrorModel baseErrorModel);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        setContentView(getContentViewResourceId());
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed()");
        if (snackbar != null && snackbar.isShown()) {
            snackbar.dismiss();
            setRequest(null);
        } else {
            super.onBackPressed();
        }
    }

    public BaseRequest getRequest() {
        Log.d(TAG, "getRequest()");
        return baseRequest;
    }

    public void setRequest(BaseRequest baseRequest) {
        Log.d(TAG, "setRequest()");
        this.baseRequest = baseRequest;
    }

    public void dismissSnackBar() {
        Log.d(TAG, "dismissSnackBar()");
        if (snackbar != null && snackbar.isShown()) {
            snackbar.dismiss();
        }
    }

    public void showSnackbar(View view, String message) {
        showSnackbar(view, message, null, Snackbar.LENGTH_INDEFINITE, null);
    }

    public void showSnackbar(View view, String message, int duration) {
        showSnackbar(view, message, null, duration, null);
    }

    public void showSnackbar(View view, String message, String buttonName,
                             View.OnClickListener onClickListener) {
        showSnackbar(view, message, buttonName, Snackbar.LENGTH_INDEFINITE, onClickListener);
    }

    public void showSnackbar(View view, String messageText, String buttonName, int duration,
                             View.OnClickListener onClickListener) {
        Log.d(TAG, "showSnackbar()");
        snackbar = Snackbar.make(view, messageText, duration);
        if (TextUtils.isEmpty(buttonName) && onClickListener != null) {
            snackbar.setAction(android.R.string.ok, onClickListener);
        }
        if (!TextUtils.isEmpty(buttonName) && onClickListener == null) {
            snackbar.setAction(buttonName, listener -> {
            });
        }
        if (TextUtils.isEmpty(buttonName) && onClickListener == null) {
            snackbar.setAction(android.R.string.ok, listener -> {
            });
        }
        if (!TextUtils.isEmpty(buttonName) && onClickListener != null) {
            snackbar.setAction(buttonName, onClickListener);
        }
        snackbar.show();
    }

    public void showToast(Context context, String message) {
        Log.d(TAG, "showToast()");
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}