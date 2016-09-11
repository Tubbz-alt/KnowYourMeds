package com.tompee.utilities.knowyourmeds.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tompee.utilities.knowyourmeds.view.fragment.SearchFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private static final int PAGE_COUNT = 2;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        return SearchFragment.newInstance();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }
}
