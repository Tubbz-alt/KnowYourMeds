package com.tompee.utilities.knowyourmeds.feature.search

import com.tompee.utilities.knowyourmeds.base.BaseView

interface SearchView : BaseView {
    fun setAdapter(adapter: SearchViewPagerAdapter)
    fun showDisclaimer(isFirst : Boolean)
    fun showAppRater()
}