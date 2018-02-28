package com.dbondarenko.shpp.notes.ui.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.dbondarenko.shpp.notes.Constants;
import com.dbondarenko.shpp.notes.R;
import com.dbondarenko.shpp.notes.ui.listeners.OnResultDialogListener;

/**
 * File: ActionDialogFragment.java
 *
 * @author Dmytro Bondarenko
 *         Date: 23.02.2018
 *         Time: 21:22
 *         E-mail: bondes87@gmail.com
 */
public class ActionDialogFragment extends DialogFragment {

    private static final String TAG = ActionDialogFragment.class.getSimpleName();

    protected String dialogTitle;
    protected String dialogMessage;
    protected String positiveButtonTitle;
    protected String negativeButtonTitle;
    private OnResultDialogListener onResultDialogListener;

    public ActionDialogFragment() {
    }

    public static ActionDialogFragment newInstance(String dialogTitle, String dialogMessage,
                                                   String positiveButtonTitle, String negativeButtonTitle) {
        ActionDialogFragment actionDialogFragment = new ActionDialogFragment();
        Bundle args = new Bundle();
        args.putString(Constants.KEY_ACTION_DIALOG_TITLE, dialogTitle);
        args.putString(Constants.KEY_ACTION_DIALOG_MESSAGE, dialogMessage);
        args.putString(Constants.KEY_ACTION_POSITIVE_BUTTON_TITLE, positiveButtonTitle);
        args.putString(Constants.KEY_ACTION_NEGATIVE_BUTTON_TITLE, negativeButtonTitle);
        actionDialogFragment.setArguments(args);
        return actionDialogFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach()");
        if (context instanceof OnResultDialogListener) {
            onResultDialogListener = (OnResultDialogListener) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            dialogTitle = args.getString(Constants.KEY_ACTION_DIALOG_TITLE);
            dialogMessage = args.getString(Constants.KEY_ACTION_DIALOG_MESSAGE);
            positiveButtonTitle = args.getString(Constants.KEY_ACTION_POSITIVE_BUTTON_TITLE);
            negativeButtonTitle = args.getString(Constants.KEY_ACTION_NEGATIVE_BUTTON_TITLE);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(TAG, "onCreateDialog()");
        AlertDialog.Builder deleteNoteDialog = new AlertDialog.Builder(getActivity());
        deleteNoteDialog.setTitle(dialogTitle)
                .setMessage(dialogMessage)
                .setIcon(R.mipmap.ic_info)
                .setPositiveButton(positiveButtonTitle,
                        (dialogInterface, okButton) -> onResultDialogListener.onDialogPositiveClicked(getTag()))
                .setNegativeButton(negativeButtonTitle,
                        (dialogInterface, cancelButton) -> onResultDialogListener.onDialogNegativeClicked(getTag()));
        return deleteNoteDialog.create();
    }
}