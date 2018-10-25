package com.tompee.utilities.knowyourmeds.feature.main.disclaimer

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProviders
import com.tompee.utilities.knowyourmeds.R
import com.tompee.utilities.knowyourmeds.base.BaseDialogFragment
import com.tompee.utilities.knowyourmeds.feature.main.MainViewModel
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class DisclaimerDialog : BaseDialogFragment() {

    @Inject
    lateinit var factory: MainViewModel.Factory

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

    override fun setupDependencies() {
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val vm = ViewModelProviders.of(activity!!)[MainViewModel::class.java]

        val view = LayoutInflater.from(context).inflate(R.layout.dialog_disclaimer, null)!!
        val isFirst = arguments?.getBoolean(TAG_FIRST_TIME) ?: false
        val builder = AlertDialog.Builder(activity!!)
                .setTitle(R.string.label_disclaimer)
                .setView(view)
                .setCancelable(false)

        if (isFirst) {
            builder.setNegativeButton(R.string.control_cancel) { _, _ ->
                vm.onDisclaimerCancelled()
            }
        }
        builder.setPositiveButton(R.string.control_understand) { _, _ ->
            vm.onDisclaimerAccepted()
        }
        return builder.create()
    }
}