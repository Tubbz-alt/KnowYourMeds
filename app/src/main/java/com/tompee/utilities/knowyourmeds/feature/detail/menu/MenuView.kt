package com.tompee.utilities.knowyourmeds.feature.detail.menu

import android.support.annotation.ColorRes
import android.support.annotation.StringRes
import com.tompee.utilities.knowyourmeds.base.BaseView
import com.tompee.utilities.knowyourmeds.model.Type

interface MenuView : BaseView {
    fun createMenuView(@StringRes stringId: Int, @ColorRes colorId: Int, type: Type)
}