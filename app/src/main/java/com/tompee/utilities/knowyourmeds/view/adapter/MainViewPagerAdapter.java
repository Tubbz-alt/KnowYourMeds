package com.tompee.utilities.knowyourmeds.view.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tompee.utilities.knowyourmeds.R;
import com.tompee.utilities.knowyourmeds.view.fragment.RecentFavoriteFragment;
import com.tompee.utilities.knowyourmeds.view.fragment.SearchFragment;
import com.tompee.utilities.knowyourmeds.view.fragment.SettingsFragment;

public class MainViewPagerAdapter extends FragmentPagerAdapter {
    private static final int PAGE_COUNT = 3;
    private final SearchFragment mSearchFragment;
    private final RecentFavoriteFragment mRecentFavoriteFragment;
    private final SettingsFragment mSettingsFragment;
    private final boolean mIsFullLayoutSupported;
    private final Context mContext;

    public MainViewPagerAdapter(Context context, FragmentManager fm, boolean isFullLayoutSupported) {
        super(fm);
        mSearchFragment = SearchFragment.newInstance();
        mRecentFavoriteFragment = RecentFavoriteFragment.newInstance();
        mSettingsFragment = SettingsFragment.newInstance();
        mContext = context;
        mIsFullLayoutSupported = isFullLayoutSupported;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return mSearchFragment;
            case 1:
                return mRecentFavoriteFragment;
            case 2:
                return mSettingsFragment;
        }
        return mSearchFragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String name = "";
        if (mIsFullLayoutSupported) {
            switch (position) {
                case 0:
                    name = mContext.getString(R.string.explore);
                    break;
                case 1:
                    name = mContext.getString(R.string.favorites);
                    break;
                case 2:
                    name = mContext.getString(R.string.settings);
                    break;
            }
        }
        return name;
    }
}
