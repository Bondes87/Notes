package com.dbondarenko.shpp.core.models;

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
    private int totalAmountOfNotesOnServer;

    public GetNotesResult(List<NoteModel> notes, int totalAmountOfNotesOnServer) {
        this.notes = notes;
        this.totalAmountOfNotesOnServer = totalAmountOfNotesOnServer;
    }

    public int getTotalAmountOfNotesOnServer() {
        return totalAmountOfNotesOnServer;
    }

    public List<NoteModel> getNotes() {
        return notes;
    }
}