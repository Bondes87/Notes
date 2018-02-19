package com.dbondarenko.shpp.notes.utils;

import android.util.Log;

import com.dbondarenko.shpp.notes.Constants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.util.Locale.US;

/**
 * File: Util.java
 *
 * @author Dmytro Bondarenko
 *         Date: 19.02.2018
 *         Time: 18:46
 *         E-mail: bondes87@gmail.com
 */
public class Util {
    private static final String TAG = Util.class.getSimpleName();

    public static String getStringDatetime(long datetime) {
        Log.d(TAG, "getStringDatetime()");
        return getFormattedString(datetime, Constants.PATTERN_DATETIME);
    }

    public static String getStringDate(long datetime) {
        Log.d(TAG, "getStringDate()");
        return getFormattedString(datetime, Constants.PATTERN_DATE);
    }

    public static String getStringTime(long datetime) {
        Log.d(TAG, "getStringTime()");
        return getFormattedString(datetime, Constants.PATTERN_TIME);
    }

    private static String getFormattedString(long datetime, String pattern) {
        DateFormat dateFormat = new SimpleDateFormat(pattern, US);
        return dateFormat.format(new Date(datetime));
    }
}