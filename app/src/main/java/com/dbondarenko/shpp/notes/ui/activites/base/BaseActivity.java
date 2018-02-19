package com.dbondarenko.shpp.notes.ui.activites.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(getContentViewResourceId());
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
}