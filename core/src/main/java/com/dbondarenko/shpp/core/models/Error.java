package com.dbondarenko.shpp.core.models;

/**
 * File: Error.java
 *
 * @author Dmytro Bondarenko
 *         Date: 05.02.2018
 *         Time: 13:33
 *         E-mail: bondes87@gmail.com
 */
public class Error {
    private String message;

    public Error(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
