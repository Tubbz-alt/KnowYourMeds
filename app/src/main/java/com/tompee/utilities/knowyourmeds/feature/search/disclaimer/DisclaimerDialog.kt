package com.tompee.utilities.knowyourmeds.feature.search.disclaimer

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import com.tompee.utilities.knowyourmeds.R
import com.tompee.utilities.knowyourmeds.base.BaseDialogFragment

class DisclaimerDialog : BaseDialogFragment() {

    companion object {
        private const val TAG_FIRST_TIME = "first_time"

        fun newInstance(first: Boolean): DisclaimerDialog {
            val dialog = DisclaimerDialog()
            val bundle = Bundle()
            bundle.putBoolean(TAG_FIRST_TIME, first)
            dialog.arguments = bundle
            return dialog
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_disclaimer, null)!!
        val isFirst = arguments?.getBoolean(TAG_FIRST_TIME) ?: false
        val builder = AlertDialog.Builder(activity!!)
                .setTitle(R.string.menu_disclaimer)
                .setView(view)
                .setCancelable(false)

        if (isFirst) {
            builder.setNegativeButton(R.string.control_cancel) { _, _ ->
                (activity as DisclaimerDialogListener).onCancelled()
            }
        }
        builder.setPositiveButton(R.string.control_understand) { _, _ ->
            (activity as DisclaimerDialogListener).onUnderstand()
        }
        return builder.create()
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