package com.dbondarenko.shpp.notes.ui.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.dbondarenko.shpp.notes.R;
import com.dbondarenko.shpp.notes.ui.listeners.OnResultDialogListener;

/**
 * File: DeleteNoteDialogFragment.java
 *
 * @author Dmytro Bondarenko
 *         Date: 23.02.2018
 *         Time: 21:22
 *         E-mail: bondes87@gmail.com
 */
public class DeleteNoteDialogFragment extends DialogFragment {

    private static final String TAG = DeleteNoteDialogFragment.class.getSimpleName();
    private OnResultDialogListener onResultDialogListener;

    public DeleteNoteDialogFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach()");
        if (context instanceof OnResultDialogListener) {
            onResultDialogListener = (OnResultDialogListener) context;
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(TAG, "onCreateDialog()");
        AlertDialog.Builder deleteNoteDialog = new AlertDialog.Builder(getActivity());
        deleteNoteDialog.setTitle(R.string.text_delete_note)
                .setMessage(R.string.text_for_dialog_to_delete_note)
                .setIcon(R.mipmap.ic_info)
                .setPositiveButton(R.string.button_ok,
                        (dialogInterface, okButton) -> onResultDialogListener.onDialogPositiveClicked())
                .setNegativeButton(R.string.button_cancel,
                        (dialogInterface, cancelButton) -> onResultDialogListener.onDialogNegativeClicked());
        return deleteNoteDialog.create();
    }
}