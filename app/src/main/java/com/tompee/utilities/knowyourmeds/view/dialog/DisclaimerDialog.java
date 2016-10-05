package com.tompee.utilities.knowyourmeds.view.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.tompee.utilities.knowyourmeds.R;

public class DisclaimerDialog extends DialogFragment {
    private static final String TAG_FIRST_TIME = "first_time";

    public static DisclaimerDialog newInstance(boolean firstTime) {
        DisclaimerDialog dialog = new DisclaimerDialog();
        Bundle bundle = new Bundle();
        bundle.putBoolean(DisclaimerDialog.TAG_FIRST_TIME, firstTime);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.dialog_disclaimer, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.menu_disclaimer);
        builder.setView(view);
        boolean firstTime = getArguments().getBoolean(TAG_FIRST_TIME);
        if (firstTime) {
            builder.setNegativeButton(R.string.control_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ((DisclaimerDialogListener) getActivity()).onCancelled();
                }
            });
        }
        builder.setPositiveButton(R.string.control_understand, firstTime ? new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((DisclaimerDialogListener) getActivity()).onUnderstand();
            }
        } : null);
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().setCancelable(false);
    }

    public interface DisclaimerDialogListener {
        void onUnderstand();
        void onCancelled();
    }
}
