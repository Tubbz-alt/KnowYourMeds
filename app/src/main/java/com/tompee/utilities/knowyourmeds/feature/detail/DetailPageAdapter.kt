package com.tompee.utilities.knowyourmeds.feature.detail

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class DetailPageAdapter(private val fragmentList: Map<String, Fragment>,
                        fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment = fragmentList.values.toList()[position]

    override fun getCount(): Int = fragmentList.size

    override fun getPageTitle(position: Int): CharSequence? = fragmentList.keys.toList()[position]
}