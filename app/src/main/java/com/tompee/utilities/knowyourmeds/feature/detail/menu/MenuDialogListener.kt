package com.tompee.utilities.knowyourmeds.feature.detail.menu

import com.tompee.utilities.knowyourmeds.model.Type

interface MenuDialogListener {
    fun onMenuClicked(type: Type)
}