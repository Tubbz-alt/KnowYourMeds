package com.tompee.utilities.knowyourmeds.feature.detail.property

import com.tompee.utilities.knowyourmeds.base.BaseView

interface PropertyView : BaseView {
    fun setIsPrescribable(isPrescribable: Boolean)
    fun setTtyText(ttyString: String)
    fun setIngredientAdapter(adapter: ListAdapter?)
    fun setSplAdapter(adapter: ListAdapter?)
}