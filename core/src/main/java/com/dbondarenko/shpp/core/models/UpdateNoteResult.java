package com.dbondarenko.shpp.core.models;

/**
 * File: UpdateNoteResult.java
 *
 * @author Dmytro Bondarenko
 *         Date: 05.02.2018
 *         Time: 14:24
 *         E-mail: bondes87@gmail.com
 */
public class UpdateNoteResult extends Result {
    private boolean isUpdated;

    public UpdateNoteResult(boolean isUpdated) {
        this.isUpdated = isUpdated;
    }

    public boolean isUpdated() {
        return isUpdated;
    }
}
