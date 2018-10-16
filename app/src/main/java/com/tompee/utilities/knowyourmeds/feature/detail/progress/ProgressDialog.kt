package com.tompee.utilities.knowyourmeds.feature.detail.progress

import android.content.Context
import android.os.Bundle
import com.tompee.utilities.knowyourmeds.R
import com.tompee.utilities.knowyourmeds.base.BaseDialog
import kotlinx.android.synthetic.main.dialog_progress.*

class ProgressDialog(context: Context,
                     private val message: String) :
        BaseDialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        window?.setBackgroundDrawableResource(R.color.colorPrimaryAlpha)
        super.onCreate(savedInstanceState)
        setCancelable(false)
        progress_dialog_text.text = message
    }

    override fun layoutId(): Int = R.layout.dialog_progress
}
