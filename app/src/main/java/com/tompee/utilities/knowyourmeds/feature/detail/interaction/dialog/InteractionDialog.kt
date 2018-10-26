package com.tompee.utilities.knowyourmeds.feature.detail.interaction.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.tompee.utilities.knowyourmeds.R
import com.tompee.utilities.knowyourmeds.base.BaseDialogFragment

class InteractionDialog : BaseDialogFragment() {

    companion object {
        private const val TAG_PARTNER = "partner"
        private const val TAG_URL = "url"
        private const val TAG_INTERACTION = "interaction"

        fun newInstance(partner: String, url: String, interaction: String): InteractionDialog {
            val fragment = InteractionDialog()
            val bundle = Bundle()
            bundle.putString(TAG_PARTNER, partner)
            bundle.putString(TAG_URL, url)
            bundle.putString(TAG_INTERACTION, interaction)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun setupDependencies() {
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity!!)
                .setTitle(arguments?.getString(TAG_PARTNER))
                .setMessage(arguments?.getString(TAG_INTERACTION))
                .setPositiveButton(R.string.control_ok, null)
                .setNeutralButton(R.string.control_more_info) { _, _ ->
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(arguments?.getString(TAG_URL)))
                    startActivity(browserIntent)
                }
                .setCancelable(false)
        return builder.create()
    }
}