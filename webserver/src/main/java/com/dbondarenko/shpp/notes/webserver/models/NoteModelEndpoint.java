package com.dbondarenko.shpp.notes.webserver.models;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Named;

/**
 * An endpoint class we are exposing
 */
@Api(
        name = "notesApi",
        version = "v1",
        resource = "notes",
        namespace = @ApiNamespace(
                ownerDomain = "models.server.shpp.dbondarenko.com",
                ownerName = "models.server.shpp.dbondarenko.com",
                packagePath = ""
        )
)
public class NoteModelEndpoint {

    private static final Logger logger = Logger.getLogger(NoteModelEndpoint.class.getName());

    private HashMap<String, NoteModel> notes;

    public NoteModelEndpoint() {
        notes = new HashMap<>();
    }

    @ApiMethod(name = "addNote")
    public void addNote(NoteModel note) {
        logger.info("Calling addNote method.");
        notes.put(note.getId(), note);
    }

    @ApiMethod(name = "getNotes")
    public List<NoteModel> getNotes() {
        logger.info("Calling getNotes method.");
        List<NoteModel> notesList = new ArrayList<>(notes.values());
        logger.info("noteList: " + notesList.toString());
        sortNotes(notesList);
        logger.info("sortedNoteList: " + notesList.toString());
        return notesList;
    }

    @ApiMethod(name = "updateNote")
    public void updateNote(NoteModel note) {
        logger.info("Calling updateNote method.");
        if (notes.containsKey(note.getId())) {
            notes.put(note.getId(), note);
        }
    }

    @ApiMethod(name = "deleteNote")
    public void deleteNote(@Named("id") String id) {
        logger.info("Calling deleteNote method ");
        if (notes.containsKey(id)) {
            notes.remove(id);
        }
    }

    private void sortNotes(List<NoteModel> notesList) {
        logger.info("Calling sortNotes method ");
        if (notesList != null) {
            Collections.sort(notesList, new Comparator<NoteModel>() {
                @Override
                public int compare(NoteModel note1, NoteModel note2) {
                    if (note1.getDatetime() == note2.getDatetime()) {
                        return 0;
                    } else if (note1.getDatetime() < note2.getDatetime()) {
                        return 1;
                    }
                    return -1;
                }
            });
        }
    }
}