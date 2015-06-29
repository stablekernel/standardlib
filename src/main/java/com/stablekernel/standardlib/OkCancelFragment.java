package com.stablekernel.standardlib;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class OkCancelFragment extends DialogFragment {
    public static final String TAG = OkCancelFragment.class.getSimpleName();

    public static final String DATA_ENTITY_ID = "DATA_ENTITY_ID";

    private static final String ARGS_ENTITY_ID = "ARGS_ENTITY_ID";
    private static final String ARGS_TITLE = "ARGS_TITLE";
    private static final String ARGS_MESSAGE = "ARGS_MESSAGE";
    private static final String ARGS_MESSAGE_ID = "ARGS_MESSAGE_ID";
    private static final String ARGS_TITLE_ID = "ARGS_TITLE_ID";

    private String title;
    private String message;
    private int entityId;
    private int messageId;

    public static OkCancelFragment newInstance(int messageId) {
        OkCancelFragment fragment = new OkCancelFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_MESSAGE_ID, messageId);
        fragment.setArguments(args);
        return fragment;
    }

    public static OkCancelFragment newInstance(int titleId, int messageId, int id) {
        OkCancelFragment fragment = new OkCancelFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_TITLE_ID, titleId);
        args.putInt(ARGS_MESSAGE_ID, messageId);
        args.putInt(ARGS_ENTITY_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    public static OkCancelFragment newInstance(String title, String message, int id) {
        OkCancelFragment fragment = new OkCancelFragment();
        Bundle args = new Bundle();
        args.putString(ARGS_TITLE, title);
        args.putString(ARGS_MESSAGE, message);
        args.putInt(ARGS_ENTITY_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    public static OkCancelFragment newInstance(String title, String message) {
        return newInstance(title, message, -1);
    }

    public static OkCancelFragment newInstance(int titleId, int messageId) {
        return newInstance(titleId, messageId, -1);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments().containsKey(ARGS_TITLE)) {
            title = getArguments().getString(ARGS_TITLE);
        } else {
            title = getArguments().getString(ARGS_TITLE_ID);
        }
        messageId = getArguments().getInt(ARGS_MESSAGE_ID, -1);
        if (messageId == -1) {
            message = getArguments().getString(ARGS_MESSAGE);
        }
        entityId = getArguments().getInt(ARGS_ENTITY_ID);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent data = new Intent();
                        data.putExtra(DATA_ENTITY_ID, entityId);
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, data);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null);

        if (messageId != -1) {
            builder.setMessage(messageId);
        } else if (message != null) {
            builder.setMessage(message);
        }

        return builder.create();
    }
}
