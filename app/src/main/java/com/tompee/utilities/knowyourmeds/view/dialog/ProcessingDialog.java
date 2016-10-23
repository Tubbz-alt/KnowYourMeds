package com.tompee.utilities.knowyourmeds.view.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tompee.utilities.knowyourmeds.R;

public class ProcessingDialog extends Dialog {

    public ProcessingDialog(Context context, String message) {
        super(context, android.R.style.Theme_Wallpaper_NoTitleBar);

        @SuppressLint("InflateParams")
        View progressDialog = LayoutInflater.from(context).inflate(R.layout.dialog_progress, null);
        TextView dialogText = (TextView) progressDialog.findViewById(R.id.progress_dialog_text);
        dialogText.setText(message);

        getWindow().setBackgroundDrawableResource(R.color.progress_dialog_background);
        setContentView(progressDialog);
        setCancelable(false);
    }

}
