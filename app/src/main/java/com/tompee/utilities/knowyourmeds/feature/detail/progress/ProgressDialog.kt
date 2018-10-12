package com.tompee.utilities.knowyourmeds.feature.detail.progress

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.tompee.utilities.knowyourmeds.R

class ProgressDialog(context: Context, message: String) :
        Dialog(context, android.R.style.Theme_Wallpaper_NoTitleBar) {

    init {
        @SuppressLint("InflateParams")
        val progressDialog = LayoutInflater.from(context).inflate(R.layout.dialog_progress, null)
        val dialogText = progressDialog.findViewById<View>(R.id.progress_dialog_text) as TextView
        dialogText.text = message

        window!!.setBackgroundDrawableResource(R.color.colorPrimaryAlpha)
        setContentView(progressDialog)
        setCancelable(false)
    }
}
