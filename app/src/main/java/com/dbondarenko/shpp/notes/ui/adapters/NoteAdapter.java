package com.dbondarenko.shpp.notes.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dbondarenko.shpp.notes.R;
import com.dbondarenko.shpp.notes.models.NoteModel;
import com.dbondarenko.shpp.notes.ui.listeners.OnEmptyListListener;
import com.dbondarenko.shpp.notes.ui.listeners.OnListItemClickListener;
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
public class NoteAdapter extends
        RecyclerView.Adapter<NoteAdapter.NoteHolder> {

    private static final String TAG = NoteAdapter.class.getSimpleName();

    private OnListItemClickListener onListItemClickListener;
    private OnEmptyListListener onEmptyListListener;
    private List<NoteModel> notesList;

    public NoteAdapter(OnListItemClickListener onListItemClickListener,
                       OnEmptyListListener onEmptyListListener) {
        notesList = new ArrayList<>();
        this.onListItemClickListener = onListItemClickListener;
        this.onEmptyListListener = onEmptyListListener;
    }

    @Override
    public NoteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder()");
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notes_list_item, parent, false);
        return new NoteHolder(itemView, onListItemClickListener);
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount()");
        return notesList.size();
    }

    @Override
    public void onBindViewHolder(NoteHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder()");
        NoteModel note = notesList.get(position);
        holder.textViewNoteMessage.setText(note.getMessage());
        holder.textViewNoteDate.setText(Util.getStringDate(note.getDatetime()));
        holder.textViewNoteTime.setText(Util.getStringTime(note.getDatetime()));
    }

    public void addNotes(List<NoteModel> notes) {
        Log.d(TAG, "addNotes()");
        notesList.addAll(notes);
        notifyItemRangeInserted(notesList.size(), notes.size());
        checkListForEmptiness();
    }

    public void addNote(NoteModel note) {
        Log.d(TAG, "addNote()");
        addNote(note, 0);
    }

    public void addNote(NoteModel note, int notePosition) {
        Log.d(TAG, "addNote()");
        notesList.add(notePosition, note);
        notifyItemInserted(notePosition);
        checkListForEmptiness();
    }

    public void deleteNote(int notePosition) {
        Log.d(TAG, "deleteNote()");
        notesList.remove(notePosition);
        notifyItemRemoved(notePosition);
        checkListForEmptiness();
    }

    public NoteModel getNote(int notePosition) {
        Log.d(TAG, "getNote()");
        return notesList.get(notePosition);
    }

    public void checkListForEmptiness() {
        onEmptyListListener.onEmptyList(notesList.size() == 0);
    }

    public static class NoteHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private static final String TAG = NoteHolder.class.getSimpleName();

        TextView textViewNoteMessage;
        TextView textViewNoteDate;
        TextView textViewNoteTime;

        private OnListItemClickListener onListItemClickListener;

        NoteHolder(View itemView, OnListItemClickListener onListItemClickListener) {
            super(itemView);
            Log.d(TAG, "NoteHolder()");
            this.onListItemClickListener = onListItemClickListener;
            initViews(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick()");
            if (onListItemClickListener != null) {
                onListItemClickListener.onClickListItem(getAdapterPosition());
            }
        }

        private void initViews(View itemView) {
            textViewNoteMessage = itemView.findViewById(R.id.textViewNoteMessage);
            textViewNoteDate = itemView.findViewById(R.id.textViewNoteDate);
            textViewNoteTime = itemView.findViewById(R.id.textViewNoteTime);
        }
    }
}