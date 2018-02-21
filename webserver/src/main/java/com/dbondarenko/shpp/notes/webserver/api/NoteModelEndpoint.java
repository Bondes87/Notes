package com.dbondarenko.shpp.notes.webserver.api;

import com.dbondarenko.shpp.core.models.AddNoteResult;
import com.dbondarenko.shpp.core.models.ApiResponse;
import com.dbondarenko.shpp.core.models.DeleteNoteResult;
import com.dbondarenko.shpp.core.models.Error;
import com.dbondarenko.shpp.core.models.GetCountNotes;
import com.dbondarenko.shpp.core.models.GetNotesResult;
import com.dbondarenko.shpp.core.models.NoteModel;
import com.dbondarenko.shpp.core.models.UpdateNoteResult;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Named;

/**
 * An endpoint class we are exposing
 */
@Api(
        name = "notesApi",
        version = "v1",
        resource = "notesList",
        namespace = @ApiNamespace(
                ownerDomain = "models.server.shpp.dbondarenko.com",
                ownerName = "models.server.shpp.dbondarenko.com",
                packagePath = ""
        )
)
public class NoteModelEndpoint {

    private static final Logger logger = Logger.getLogger(NoteModelEndpoint.class.getName());

    private List<NoteModel> notesList;

    public NoteModelEndpoint() {
        notesList = new ArrayList<>();
    }

    @ApiMethod(name = "addNote")
    public ApiResponse addNote(NoteModel note) {
        logger.info("Calling addNote method.");
        if (note == null) {
            return new ApiResponse(new Error("Note is null."));
        }
        if (note.getMessage() == null) {
            return new ApiResponse(new Error("Note message is empty."));
        }
        if (notesList.contains(note)) {
            return new ApiResponse(new Error("This note already exists."));
        }
        notesList.add(0, note);
        return new ApiResponse(new AddNoteResult(true));
    }

    @ApiMethod(name = "getAllNotes")
    public ApiResponse getAllNotes() {
        logger.info("Calling getAllNotes method.");
        if (notesList.size() == 0) {
            return new ApiResponse(new Error("List of notes is empty."));
        }
        return new ApiResponse(new GetNotesResult(notesList));
    }

    @ApiMethod(name = "getNotes",
            httpMethod = ApiMethod.HttpMethod.GET)
    public ApiResponse getNotes(@Named("startPosition") int startPosition, @Named("amount") int amount) {
        logger.info("Calling getNotes method.");
        if (notesList.size() == 0) {
            return new ApiResponse(new Error("List of notes is empty."));
        }
        if (startPosition < 0 || amount < 0) {
            return new ApiResponse(new Error("One of the specified parameters is less than 0."));
        }
        if (startPosition == notesList.size()) {
            return new ApiResponse(new GetNotesResult(new ArrayList<NoteModel>()));
        }
        if (startPosition > notesList.size()) {
            return new ApiResponse(new Error("Starting position is longer for the length of the list."));
        }
        if (amount == 0) {
            return new ApiResponse(new Error("The amount must not be zero."));
        }
        int endPosition = startPosition + amount;
        if (endPosition > notesList.size()) {
            return new ApiResponse(new GetNotesResult(notesList.subList(startPosition, notesList.size())));
        }
        return new ApiResponse(new GetNotesResult(notesList.subList(startPosition, endPosition)));
    }

    @ApiMethod(name = "getCountNotes",
            httpMethod = ApiMethod.HttpMethod.GET,
            path = "notesList/count")
    public ApiResponse getCountNotes() {
        logger.info("Calling getCountNotes method.");
        return new ApiResponse(new GetCountNotes(notesList.size()));
    }

    @ApiMethod(name = "updateNote")
    public ApiResponse updateNote(NoteModel note) {
        logger.info("Calling updateNote method.");
        if (note == null) {
            return new ApiResponse(new Error("Note is null."));
        }
        if (note.getMessage() == null) {
            return new ApiResponse(new Error("Note message is empty."));
        }
        int indexOfNoteFromList = getIndexOfNoteByDatetime(note.getDatetime());
        if (indexOfNoteFromList != -1) {
            notesList.get(indexOfNoteFromList).setMessage(note.getMessage());
            return new ApiResponse(new UpdateNoteResult(true));
        }
        return new ApiResponse(new Error("Note does not exist."));
    }

    @ApiMethod(name = "deleteNote")
    public ApiResponse deleteNote(@Named("datetime") long datetime) {
        logger.info("Calling deleteNote method ");
        if (notesList.size() == 0) {
            return new ApiResponse(new Error("List of notes is empty."));
        }
        int indexOfNoteFromList = getIndexOfNoteByDatetime(datetime);
        if (indexOfNoteFromList != -1) {
            notesList.remove(indexOfNoteFromList);
            return new ApiResponse(new DeleteNoteResult(true));
        }
        return new ApiResponse(new Error("Note does not exist."));
    }

    private int getIndexOfNoteByDatetime(long datetime) {
        for (NoteModel noteFromList : notesList) {
            if (noteFromList.getDatetime() == datetime) {
                return notesList.indexOf(noteFromList);
            }
        }
        return -1;
    }
}