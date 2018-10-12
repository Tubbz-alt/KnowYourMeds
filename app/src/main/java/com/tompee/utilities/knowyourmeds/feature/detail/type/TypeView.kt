package com.tompee.utilities.knowyourmeds.feature.detail.type

import com.tompee.utilities.knowyourmeds.base.BaseView
import com.tompee.utilities.knowyourmeds.model.Type

interface TypeView : BaseView {
    fun getType(): Type
    fun setAdapter(adapter: ListAdapter)
}