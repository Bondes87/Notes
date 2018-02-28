package com.dbondarenko.shpp.notes.models.exception;

/**
 * File: NoInternetConnectionException.java
 *
 * @author Dmytro Bondarenko
 *         Date: 27.02.2018
 *         Time: 12:24
 *         E-mail: bondes87@gmail.com
 */
public class NoInternetConnectionException extends Exception {
    private String message;

    public NoInternetConnectionException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}