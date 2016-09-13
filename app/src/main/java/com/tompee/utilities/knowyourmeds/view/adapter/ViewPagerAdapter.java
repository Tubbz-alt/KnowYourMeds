package com.tompee.utilities.knowyourmeds.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tompee.utilities.knowyourmeds.view.fragment.SearchFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private static final int PAGE_COUNT = 1;
    private final SearchFragment mSearchFragment;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        mSearchFragment = SearchFragment.newInstance();
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        return mSearchFragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }
}
