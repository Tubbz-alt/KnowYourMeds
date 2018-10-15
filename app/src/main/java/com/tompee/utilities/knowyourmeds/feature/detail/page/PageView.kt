package com.tompee.utilities.knowyourmeds.feature.detail.page

import com.tompee.utilities.knowyourmeds.base.BaseView

interface PageView : BaseView {
    fun loadUrl(url: String)
}