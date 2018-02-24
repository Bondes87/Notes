package com.dbondarenko.shpp.notes.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dbondarenko.shpp.notes.Constants;
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
        RecyclerView.Adapter<NoteAdapter.BaseHolder> {

    private static final String TAG = NoteAdapter.class.getSimpleName();

    private OnListItemClickListener onListItemClickListener;
    private OnEmptyListListener onEmptyListListener;
    private List<NoteModel> notesList;
    private boolean isEnabledFooter;

    public NoteAdapter(OnListItemClickListener onListItemClickListener,
                       OnEmptyListListener onEmptyListListener) {
        notesList = new ArrayList<>();
        this.onListItemClickListener = onListItemClickListener;
        this.onEmptyListListener = onEmptyListListener;
    }

    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder()");
        View itemView;
        switch (viewType) {
            case Constants.TYPE_ITEM:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.notes_list_item, parent, false);
                return new NoteHolder(itemView, onListItemClickListener);
            case Constants.TYPE_FOOTER:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.notes_list_footer, parent, false);
                return new FooterHolder(itemView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(BaseHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder()");
        if (holder.getItemViewType() == Constants.TYPE_ITEM) {
            NoteModel note = notesList.get(position);
            ((NoteHolder) holder).textViewNoteMessage.setText(note.getMessage());
            ((NoteHolder) holder).textViewNoteDate.setText(
                    Util.getStringDate(note.getDatetime()));
            ((NoteHolder) holder).textViewNoteTime.setText(
                    Util.getStringTime(note.getDatetime()));
        }
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount()");
        if (isEnabledFooter) {
            return notesList.size() + 1;
        } else {
            return notesList.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionFooter(position)) {
            return Constants.TYPE_FOOTER;
        }
        return Constants.TYPE_ITEM;
    }

    public void setEnabledFooter(boolean isEnabled) {
        if (isEnabled) {
            notifyItemInserted(notesList.size());
        } else {
            notifyItemRemoved(notesList.size());
        }
        isEnabledFooter = isEnabled;
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
        if (isEnabledFooter) {
            return notesList.get(notePosition - 1);
        }
        return notesList.get(notePosition);
    }

    public List<NoteModel> getNotes() {
        Log.d(TAG, "getNotes()");
        return notesList;
    }

    public void checkListForEmptiness() {
        onEmptyListListener.onEmptyList(notesList.size() == 0);
    }

    private boolean isPositionFooter(int position) {
        return position == notesList.size();
    }

    public static class BaseHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        BaseHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View v) {
        }
    }

    public static class NoteHolder extends BaseHolder {

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
            Log.d(TAG, "initViews()");
            textViewNoteMessage = itemView.findViewById(R.id.textViewNoteMessage);
            textViewNoteDate = itemView.findViewById(R.id.textViewNoteDate);
            textViewNoteTime = itemView.findViewById(R.id.textViewNoteTime);
        }
    }

    static class FooterHolder extends BaseHolder {

        private static final String TAG = FooterHolder.class.getSimpleName();

        ProgressBar progressBarNotesListFooter;

        FooterHolder(View itemView) {
            super(itemView);
            Log.d(TAG, "FooterHolder()");
            initViews(itemView);
        }

        private void initViews(View itemView) {
            Log.d(TAG, "initViews()");
            progressBarNotesListFooter = itemView.findViewById(R.id.progressBarNotesListFooter);
        }
    }
}