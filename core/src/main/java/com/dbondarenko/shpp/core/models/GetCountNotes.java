package com.dbondarenko.shpp.core.models;

/**
 * File: GetCountNotes.java
 *
 * @author Dmytro Bondarenko
 *         Date: 21.02.2018
 *         Time: 11:58
 *         E-mail: bondes87@gmail.com
 */
public class GetCountNotes extends Result {
    private int count;

    public GetCountNotes(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }
}