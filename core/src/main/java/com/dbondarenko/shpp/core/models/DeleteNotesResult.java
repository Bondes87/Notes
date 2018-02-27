package com.dbondarenko.shpp.core.models;

/**
 * File: DeleteNotesResult.java
 *
 * @author Dmytro Bondarenko
 *         Date: 27.02.2018
 *         Time: 22:45
 *         E-mail: bondes87@gmail.com
 */
public class DeleteNotesResult extends Result {
    private boolean isDeleted;

    public DeleteNotesResult(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public boolean isDeleted() {
        return isDeleted;
    }
}