package com.dbondarenko.shpp.notes.webserver.api;

import com.dbondarenko.shpp.notes.webserver.models.AddNoteResult;
import com.dbondarenko.shpp.notes.webserver.models.ApiResponse;
import com.dbondarenko.shpp.notes.webserver.models.DeleteNoteResult;
import com.dbondarenko.shpp.notes.webserver.models.Error;
import com.dbondarenko.shpp.notes.webserver.models.GetNotesResult;
import com.dbondarenko.shpp.notes.webserver.models.NoteModel;
import com.dbondarenko.shpp.notes.webserver.models.UpdateNoteResult;
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
    public ApiResponse addNote(NoteModel note) {
        logger.info("Calling addNote method.");
        if (note == null) {
            return new ApiResponse(new Error("Note is null."));
        }

        if (note.getId() == null || note.getMessage() == null) {
            return new ApiResponse(new Error("One of field of note are empty."));
        }
        if (notes.containsKey(note.getId())) {
            return new ApiResponse(new Error("Note with such an id already exists."));
        }
        notes.put(note.getId(), note);
        return new ApiResponse(new AddNoteResult(true));
    }

    @ApiMethod(name = "getNotes")
    public ApiResponse getNotes() {
        logger.info("Calling getNotes method.");
        if (notes.size() == 0) {
            return new ApiResponse(new Error("No notes."));
        }
        List<NoteModel> notesList = new ArrayList<>(notes.values());
        logger.info("noteList: " + notesList.toString());
        sortNotes(notesList);
        logger.info("sortedNoteList: " + notesList.toString());
        return new ApiResponse(new GetNotesResult(notesList));
    }

    @ApiMethod(name = "updateNote")
    public ApiResponse updateNote(NoteModel note) {
        logger.info("Calling updateNote method.");
        if (note == null) {
            return new ApiResponse(new Error("Note is null."));
        }
        if (note.getId() == null || note.getMessage() == null) {
            return new ApiResponse(new Error("One of field of note are empty."));
        }
        if (notes.containsKey(note.getId())) {
            notes.put(note.getId(), note);
            return new ApiResponse(new UpdateNoteResult(true));
        }
        return new ApiResponse(new Error("Note does not exist."));
    }

    @ApiMethod(name = "deleteNote")
    public ApiResponse deleteNote(@Named("id") String id) {
        logger.info("Calling deleteNote method ");
        if (id == null) {
            return new ApiResponse(new Error("Id is null."));
        }
        if (notes.size() == 0) {
            return new ApiResponse(new Error("No notes."));
        }
        if (notes.containsKey(id)) {
            notes.remove(id);
            return new ApiResponse(new DeleteNoteResult(true));
        }
        return new ApiResponse(new Error("Note does not exist."));
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