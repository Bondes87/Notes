package com.dbondarenko.shpp.notes.webserver.models;

/**
 * File: NoteModel.java
 *
 * @author Dmytro Bondarenko
 *         Date: 04.02.2018
 *         Time: 12:12
 *         E-mail: bondes87@gmail.com
 */
public class NoteModel {
    private String id;
    private long datetime;
    private String message;

    public NoteModel() {
    }

    public NoteModel(String id, long datetime, String message) {
        this.id = id;
        this.datetime = datetime;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getDatetime() {
        return datetime;
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}