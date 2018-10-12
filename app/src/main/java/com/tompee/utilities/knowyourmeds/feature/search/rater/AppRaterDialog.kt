package com.tompee.utilities.knowyourmeds.feature.search.rater

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.tompee.utilities.knowyourmeds.BuildConfig
import com.tompee.utilities.knowyourmeds.R
import com.tompee.utilities.knowyourmeds.base.BaseDialogFragment

class AppRaterDialog : BaseDialogFragment() {

    companion object {
        fun newInstance(): AppRaterDialog {
            return AppRaterDialog()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(activity!!)
                .setTitle(R.string.ids_title_rate)
                .setMessage(R.string.ids_message_rate)
                .setNeutralButton(R.string.ids_lbl_remind, null)
                .setNegativeButton(R.string.ids_lbl_no_rate, null)
                .setPositiveButton(R.string.ids_lbl_yes_rate) { _, _ ->
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

    //region BaseDialogFragment

    override fun setupComponent() {
    }
    //endregion
}