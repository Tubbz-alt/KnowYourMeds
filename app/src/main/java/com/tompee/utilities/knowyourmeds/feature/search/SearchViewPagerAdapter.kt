package com.tompee.utilities.knowyourmeds.feature.search

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.tompee.utilities.knowyourmeds.R
import com.tompee.utilities.knowyourmeds.feature.search.persist.RecentFavoriteFragment

class SearchViewPagerAdapter(private val context: Context,
                             private val recent: RecentFavoriteFragment,
                             private val favorite: RecentFavoriteFragment,
                             fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    companion object {
        private const val PAGE_COUNT = 2
    }

    override fun getItem(index: Int): Fragment = if (index == 0) recent else favorite

    override fun getCount(): Int = PAGE_COUNT

    override fun getPageTitle(position: Int): CharSequence? = if (position == 0)
        context.getString(R.string.recent) else context.getString(R.string.favorites)
}