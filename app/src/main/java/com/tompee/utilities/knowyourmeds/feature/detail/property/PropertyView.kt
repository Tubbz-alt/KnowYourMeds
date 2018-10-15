package com.tompee.utilities.knowyourmeds.feature.detail.property

import com.tompee.utilities.knowyourmeds.base.BaseView
import com.tompee.utilities.knowyourmeds.model.Type

interface PropertyView : BaseView {
    fun setIsPrescribable(isPrescribable: Boolean)
    fun setType(type: Type)
    fun setIngredientAdapter(adapter: ListAdapter?)
    fun setSplAdapter(adapter: MarketDrugAdapter?)
}