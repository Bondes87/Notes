package com.dbondarenko.shpp.notes.api;

import com.dbondarenko.shpp.notes.api.response.GetNotesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * File: ApiService.java
 *
 * @author Dmytro Bondarenko
 *         Date: 21.02.2018
 *         Time: 10:05
 *         E-mail: bondes87@gmail.com
 */
public interface ApiService {

    @GET("/_ah/api/notes/v1/getAllNotes")
    Call<GetNotesResponse> getAllNotes();

    @GET("/_ah/api/notes/v1/getNotes")
    Call<GetNotesResponse> getNotes(@Query("startPosition") int startPosition, @Query("amount") int amount);
}
