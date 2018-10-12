package com.tompee.utilities.knowyourmeds.feature.detail

import android.support.v4.app.Fragment
import com.tompee.utilities.knowyourmeds.base.BaseView
import com.tompee.utilities.knowyourmeds.model.Type
import io.reactivex.Observable

interface DetailView : BaseView {
    fun setTitle(title: String)
    fun fragmentType(): Observable<Type>
    fun showProcessingDialog()
    fun hideProcessingDialog()
    fun setActiveFragment(fragment: Fragment)
}