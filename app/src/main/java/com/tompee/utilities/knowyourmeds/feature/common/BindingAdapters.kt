@file:Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")

package com.tompee.utilities.knowyourmeds.feature.common

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion
import com.tompee.utilities.knowyourmeds.R
import com.tompee.utilities.knowyourmeds.model.MedicineType

@BindingAdapter("visibleGone")
fun showHide(view: View, isVisible: java.lang.Boolean?) {
    view.visibility = if (isVisible != null && isVisible.booleanValue()) View.VISIBLE else View.GONE
}

@BindingAdapter("intText")
fun setIntText(view: TextView, value: Int) {
    view.text = value.toString()
}

@BindingAdapter("prescribable")
fun setPrescribableText(view: TextView, isPrescribable: Boolean) {
    view.text = if (isPrescribable) view.context.getString(R.string.property_prescribable_yes) else
        view.context.getString(R.string.property_prescribable_no)
}

@BindingAdapter("medType")
fun setMedicineTypeText(view: TextView, type: MedicineType?) {
    if (type != null) {
        view.text = type.name(view.context)
    }
}

@BindingConversion
fun listToVisibility(list: List<*>?): Int {
    return if (list == null || list.isEmpty()) View.INVISIBLE else View.VISIBLE
}