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

    // Keys for sending and receiving data from intent or bundle.
    public static final String KEY_NOTE = "KeyNote";
    public static final String KEY_NOTE_POSITION = "KeyNotePosition";
    public static final String KEY_REQUEST = "KeyRequest";
    public static final String KEY_NOTES_LIST = "KeyNotesList";
    public static final String KEY_TOTAL_AMOUNT_OF_NOTES_ON_SERVER = "KeyTotalAmountOfNotesOnServer";

    // Identifiers for async task loaders
    public static final int LOADER_ID_API_SERVICE = 1;

    // Tag for creating dialog fragment for confirmation to delete note.
    public static final String TAG_OF_DELETE_NOTE_DIALOG_FRAGMENT = "DeleteNoteDialogFragment";

    // Codes for exchanging data between activities.
    public static final int REQUEST_CODE_NOTE_ACTIVITY = 101;
    public static final int RESULT_CODE_ADD_NOTE = 102;
    public static final int RESULT_CODE_UPDATE_NOTE = 103;
    public static final int RESULT_CODE_DELETE_NOTE = 104;

    // The minimum number of items to have below your current scroll position
    // before loading more.
    public static final int MAXIMUM_COUNT_OF_NOTES_TO_LOAD = 20;
    public static final int VISIBLE_THRESHOLD = 10;

    // The identifiers of holders type.
    public static final int TYPE_ITEM = 0;
    public static final int TYPE_FOOTER = 1;
}