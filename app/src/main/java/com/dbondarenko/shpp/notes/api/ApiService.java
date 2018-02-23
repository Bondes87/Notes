package com.dbondarenko.shpp.notes.api;

import com.dbondarenko.shpp.notes.api.response.AddNoteResponse;
import com.dbondarenko.shpp.notes.api.response.DeleteNoteResponse;
import com.dbondarenko.shpp.notes.api.response.GetNotesResponse;
import com.dbondarenko.shpp.notes.api.response.UpdateNoteResponse;
import com.dbondarenko.shpp.notes.models.NoteModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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

    @POST("/_ah/api/notes/v1/addNote")
    Call<AddNoteResponse> addNote(@Body NoteModel note);

    @PUT("/_ah/api/notes/v1/updateNote")
    Call<UpdateNoteResponse> updateNote(@Body NoteModel note);

    @DELETE("/_ah/api/notes/v1/deleteNote")
    Call<DeleteNoteResponse> deleteNote(@Query("datetime") int datetime);
}
