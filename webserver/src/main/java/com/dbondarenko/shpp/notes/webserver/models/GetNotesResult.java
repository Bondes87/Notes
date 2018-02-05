package com.dbondarenko.shpp.notes.webserver.models;

import java.util.List;

/**
 * File: GetNotesResult.java
 *
 * @author Dmytro Bondarenko
 *         Date: 05.02.2018
 *         Time: 14:31
 *         E-mail: bondes87@gmail.com
 */
public class GetNotesResult extends Result {
    private List<NoteModel> notes;

    public GetNotesResult(List<NoteModel> notes) {
        this.notes = notes;
    }

    public List<NoteModel> getNotes() {
        return notes;
    }
}