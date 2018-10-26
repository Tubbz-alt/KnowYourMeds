package com.tompee.utilities.knowyourmeds.feature.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.tompee.utilities.knowyourmeds.feature.main.cache.CacheFragment

class MainPagerAdapter(fragmentManager: FragmentManager,
                       private val recent: CacheFragment,
                       private val favorite: CacheFragment) :
        FragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment = if (position == 0) recent else favorite

    override fun getCount(): Int = 2
}