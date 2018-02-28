package com.dbondarenko.shpp.notes.api;

import com.dbondarenko.shpp.notes.Constants;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * File: RetrofitHelper.java
 *
 * @author Dmytro Bondarenko
 *         Date: 22.02.2018
 *         Time: 16:50
 *         E-mail: bondes87@gmail.com
 */
public class RetrofitHelper {

    private Retrofit retrofit;

    private RetrofitHelper() {
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory
                        .create(new GsonBuilder()
                                .excludeFieldsWithoutExposeAnnotation()
                                .serializeNulls()
                                .create()));
        retrofit = retrofitBuilder.build();
    }

    public static RetrofitHelper getInstance() {
        return new RetrofitHelper();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}