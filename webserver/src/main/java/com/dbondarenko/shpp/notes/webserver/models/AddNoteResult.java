package com.dbondarenko.shpp.notes.webserver.models;

/**
 * File: AddNoteResult.java
 *
 * @author Dmytro Bondarenko
 *         Date: 05.02.2018
 *         Time: 13:36
 *         E-mail: bondes87@gmail.com
 */
public class AddNoteResult extends Result {
    private boolean isAdded;

    public AddNoteResult(boolean isAdded) {
        this.isAdded = isAdded;
    }

    public boolean isAdded() {
        return isAdded;
    }
}
