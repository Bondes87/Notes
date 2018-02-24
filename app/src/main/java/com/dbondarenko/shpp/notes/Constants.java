package com.dbondarenko.shpp.notes;

/**
 * File: Constants.java
 *
 * @author Dmytro Bondarenko
 *         Date: 19.02.2018
 *         Time: 18:47
 *         E-mail: bondes87@gmail.com
 */
public class Constants {
    // The patterns for displaying the date and time on the screen.
    public static final String PATTERN_DATETIME = "MMM dd yyyy, HH:mm";
    public static final String PATTERN_DATE = "dd-MM-yyyy";
    public static final String PATTERN_TIME = "HH:mm";

    // The keys for getting a note and note position from the arguments of the fragment.
    public static final String KEY_NOTE = "KeyNote";
    public static final String KEY_NOTE_POSITION = "KeyNotePosition";
    public static final String KEY_START_NOTE_POSITION = "KeyStartNotePosition";
    public static final String KEY_REQUEST = "KeyRequest";
    public static final String KEY_NOTES_LIST = "KeyNotesList";
    public static final String KEY_TOTAL_AMOUNT_OF_NOTES_ON_SERVER = "KeyTotalAmountOfNotesOnServer";

    // Identifiers for async task loaders
    public static final int LOADER_ID_API_SERVICE = 1;

    public static final int AMOUNT_OF_NOTES_TO_DOWNLOAD = 5;


}