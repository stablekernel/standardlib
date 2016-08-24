package com.stablekernel.standardlib;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import rx.Subscription;

public class BlockingProgressFragment extends DialogFragment {
    public static final String TAG = BlockingProgressFragment.class.getSimpleName();
    private static final String ARGS_PROGRESS_TEXT = "ARGS_PROGRESS_TEXT";
    private Subscription subscription;
    private String progressText;

    public static BlockingProgressFragment newInstance() {
        return new BlockingProgressFragment();
    }

    public static BlockingProgressFragment newInstance(String progressText) {
        BlockingProgressFragment fragment = new BlockingProgressFragment();
        Bundle args = new Bundle();
        args.putString(ARGS_PROGRESS_TEXT, progressText);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            progressText = getArguments().getString(ARGS_PROGRESS_TEXT);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_blocking_progress, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (progressText != null) {
            TextView progressTextView = (TextView) view.findViewById(R.id.progress_text);
            progressTextView.setText(progressText);
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (subscription != null) {
            subscription.unsubscribe();
            subscription = null;
        }
    }

    public void setOnCancelListener(Subscription subscription) {
        this.subscription = subscription;
    }
}
