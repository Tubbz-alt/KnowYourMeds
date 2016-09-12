package com.tompee.utilities.knowyourmeds.view.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tompee.utilities.knowyourmeds.R;

public class ProcessingDialog extends Dialog {

    public ProcessingDialog(Context context, String message) {
        super(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);

        @SuppressLint("InflateParams")
        View progressDialog = LayoutInflater.from(context).inflate(R.layout.dialog_progess, null);
        TextView dialogText = (TextView) progressDialog.findViewById(R.id.progress_dialog_text);
        dialogText.setText(message);

        getWindow().setBackgroundDrawableResource(R.color.progress_dialog_background);
        setContentView(progressDialog);
        setCancelable(false);
    }
}
