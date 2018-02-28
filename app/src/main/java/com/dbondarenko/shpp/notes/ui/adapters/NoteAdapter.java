package com.dbondarenko.shpp.notes.ui.adapters;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dbondarenko.shpp.notes.R;
import com.dbondarenko.shpp.notes.models.NoteModel;
import com.dbondarenko.shpp.notes.ui.listeners.OnEmptyListListener;
import com.dbondarenko.shpp.notes.utils.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * File: NoteAdapter.java
 *
 * @author Dmytro Bondarenko
 *         Date: 19.02.2018
 *         Time: 18:34
 *         E-mail: bondes87@gmail.com
 */
public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder> {

    private static final String TAG = NoteAdapter.class.getSimpleName();

    private OnEmptyListListener onEmptyListListener;
    private View.OnClickListener onClickListener;
    private View.OnLongClickListener onLongClickListener;
    private List<Integer> multiSelectNotesPositionsList;
    private List<NoteModel> notesList;

    public NoteAdapter(OnEmptyListListener onEmptyListListener,
                       View.OnLongClickListener onLongClickListener,
                       View.OnClickListener onClickListener) {
        notesList = new ArrayList<>();
        multiSelectNotesPositionsList = new ArrayList<>();
        this.onEmptyListListener = onEmptyListListener;
        this.onLongClickListener = onLongClickListener;
        this.onClickListener = onClickListener;
    }

    @Override
    public NoteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder()");
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notes_list_item, parent, false);
        NoteHolder noteHolder = new NoteHolder(itemView);
        itemView.setOnLongClickListener(onLongClickListener);
        itemView.setOnClickListener(onClickListener);
        return noteHolder;
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount()");
        return notesList.size();
    }

    @Override
    public void onBindViewHolder(NoteHolder holder, int position) {
        SparseBooleanArray selectedItemsIds = new SparseBooleanArray();
        selectedItemsIds.get(position);

        Log.d(TAG, "onBindViewHolder()");
        NoteModel note = notesList.get(position);
        holder.textViewNoteMessage.setText(note.getMessage());
        holder.textViewNoteDate.setText(Util.getStringDate(note.getDatetime()));
        holder.textViewNoteTime.setText(Util.getStringTime(note.getDatetime()));
        holder.itemView.setActivated(multiSelectNotesPositionsList.contains(position));
    }

    public void addMultiSelectNote(int position) {
        if (multiSelectNotesPositionsList.contains(position)) {
            multiSelectNotesPositionsList.remove(multiSelectNotesPositionsList.indexOf(position));
        } else {
            multiSelectNotesPositionsList.add(position);
        }
        notifyItemChanged(position);
    }

    public void addMultiSelectNotes(List<Integer> multiSelectNotesPositionsList) {
        this.multiSelectNotesPositionsList.addAll(multiSelectNotesPositionsList);
        for (int positionOfNotesList : multiSelectNotesPositionsList) {
            notifyItemChanged(positionOfNotesList);
        }
    }

    public void clearMultiSelectNotes() {
        for (int positionOfNotesList : multiSelectNotesPositionsList) {
            notifyItemChanged(positionOfNotesList);
        }
        multiSelectNotesPositionsList.clear();
        // notifyDataSetChanged();
    }

    public int getMultiSelectedCount() {
        return multiSelectNotesPositionsList.size();
    }

    public List<Integer> getMultiSelectNotesPositions() {
        return multiSelectNotesPositionsList;
    }

    public List<NoteModel> getMultiSelectNotes() {
        List<NoteModel> multiSelectNotesList = new ArrayList<>();
        for (int positionOfNotesList : multiSelectNotesPositionsList) {
            multiSelectNotesList.add(notesList.get(positionOfNotesList));
        }
        return multiSelectNotesList;
    }

    public void addNotes(List<NoteModel> notes) {
        Log.d(TAG, "addNotes()");
        notesList.addAll(notes);
        notifyItemRangeInserted(notesList.size(), notes.size());
        checkListForEmptiness();
    }

    public void addNote(NoteModel note) {
        Log.d(TAG, "addNote() " + note);
        addNote(note, 0);
    }

    public void addNote(NoteModel note, int notePosition) {
        Log.d(TAG, "addNote(): " + note + ", " + notePosition);
        notesList.add(notePosition, note);
        notifyItemInserted(notePosition);
        checkListForEmptiness();
    }

    public void deleteNote(int notePosition) {
        Log.d(TAG, "deleteNote() " + notePosition);
        notesList.remove(notePosition);
        notifyItemRemoved(notePosition);
        checkListForEmptiness();
    }

    public void updateNote(NoteModel note, int notePosition) {
        Log.d(TAG, "updateNote(): " + note + ", " + notePosition);
        NoteModel oldNote = getNote(notePosition);
        oldNote.setMessage(note.getMessage());
        notifyItemChanged(notePosition);
    }

    public NoteModel getNote(int notePosition) {
        Log.d(TAG, "getNote() " + notePosition);
        return notesList.get(notePosition);
    }

    public void clearNotesFromAdapter() {
        int size = notesList.size();
        notesList.clear();
        notifyItemRangeRemoved(0, size);
    }

    public List<NoteModel> getNotes() {
        Log.d(TAG, "getNotes()");
        return notesList;
    }

    public void checkListForEmptiness() {
        onEmptyListListener.onEmptyList(notesList.size() == 0);
    }

    public static class NoteHolder extends RecyclerView.ViewHolder {

        private static final String TAG = NoteHolder.class.getSimpleName();

        public ConstraintLayout constraintLayoutForeground;
        private TextView textViewNoteMessage;
        private TextView textViewNoteDate;
        private TextView textViewNoteTime;

        NoteHolder(View itemView) {
            super(itemView);
            Log.d(TAG, "NoteHolder()");
            initViews(itemView);
        }

        private void initViews(View itemView) {
            constraintLayoutForeground = itemView.findViewById(R.id.constraintLayoutForeground);
            textViewNoteMessage = itemView.findViewById(R.id.textViewNoteMessage);
            textViewNoteDate = itemView.findViewById(R.id.textViewNoteDate);
            textViewNoteTime = itemView.findViewById(R.id.textViewNoteTime);
        }
    }
}