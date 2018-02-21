package com.dbondarenko.shpp.core.models;

/**
 * File: DeleteNoteResult.java
 *
 * @author Dmytro Bondarenko
 *         Date: 05.02.2018
 *         Time: 14:25
 *         E-mail: bondes87@gmail.com
 */
public class DeleteNoteResult extends Result {
    private boolean isDeleted;

    public DeleteNoteResult(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public boolean isDeleted() {
        return isDeleted;
    }
}
