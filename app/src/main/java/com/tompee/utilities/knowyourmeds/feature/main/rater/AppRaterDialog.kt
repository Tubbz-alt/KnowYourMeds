package com.tompee.utilities.knowyourmeds.feature.main.rater

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.tompee.utilities.knowyourmeds.BuildConfig
import com.tompee.utilities.knowyourmeds.R
import com.tompee.utilities.knowyourmeds.base.BaseDialogFragment
import dagger.android.support.AndroidSupportInjection

class AppRaterDialog : BaseDialogFragment() {

    companion object {
        fun newInstance(): AppRaterDialog {
            return AppRaterDialog()
        }
    }

    override fun setupDependencies() {
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(activity!!)
                .setTitle(R.string.label_rater)
                .setMessage(R.string.message_rate)
                .setNeutralButton(R.string.control_remind, null)
                .setNegativeButton(R.string.control_no_rate, null)
                .setPositiveButton(R.string.control_yes_rate) { _, _ ->
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID))
                    startActivity(intent)
                }
                .setCancelable(false)
                .create()
    }

    override fun onResume() {
        super.onResume()
        dialog.setCancelable(false)
    }
}