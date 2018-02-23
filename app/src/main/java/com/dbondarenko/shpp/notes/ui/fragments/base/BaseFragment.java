package com.dbondarenko.shpp.notes.ui.fragments.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dbondarenko.shpp.notes.api.ApiName;
import com.dbondarenko.shpp.notes.api.response.model.base.BaseErrorModel;
import com.dbondarenko.shpp.notes.api.response.model.base.BaseResultModel;
import com.dbondarenko.shpp.notes.ui.activites.base.BaseActivity;

/**
 * File: BaseFragment.java
 *
 * @author Dmytro Bondarenko
 *         Date: 19.02.2018
 *         Time: 16:18
 *         E-mail: bondes87@gmail.com
 */
public abstract class BaseFragment extends Fragment {

    protected final String TAG;

    public BaseFragment() {
        TAG = getClass().getSimpleName();
    }

    public abstract View getContentView(LayoutInflater inflater, @Nullable ViewGroup container,
                                        @Nullable Bundle savedInstanceState);

    public abstract void handleSuccessResult(ApiName apiName, BaseResultModel baseResultModel);

    public abstract void handleFailureResult(BaseErrorModel baseErrorModel);

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        return getContentView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach");
        super.onDetach();
    }

    public BaseActivity getBaseActivity() {
        Log.d(TAG, "getBaseActivity");
        return (BaseActivity) getActivity();
    }

    public void showMessageInSnackbar(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction(android.R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
        snackbar.show();
    }

    public void showMessageInSnackbar(View view, String message, View.OnClickListener onClickListener) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE)
                .setAction(android.R.string.ok, onClickListener);
        snackbar.show();
    }
}