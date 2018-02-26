package com.dbondarenko.shpp.notes.ui.activites.base;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dbondarenko.shpp.notes.api.ApiName;
import com.dbondarenko.shpp.notes.api.response.model.base.BaseErrorModel;
import com.dbondarenko.shpp.notes.api.response.model.base.BaseResultModel;

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

    public BaseActivity() {
        TAG = getClass().getSimpleName();
    }

    public abstract int getContentViewResourceId();

    public abstract void handleSuccessResult(ApiName apiName, BaseResultModel baseResultModel);

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

    public void showMessageInSnackbar(View view, String message) {
        Log.d(TAG, "showMessageInSnackbar()");
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE)
                .setAction(android.R.string.ok, listener -> {
                });
        snackbar.show();
    }

    public void showMessageInToast(Context context, String message) {
        Log.d(TAG, "showMessageInToast()");
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}