package com.tompee.utilities.knowyourmeds.view.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tompee.utilities.knowyourmeds.R;
import com.tompee.utilities.knowyourmeds.view.fragment.RecentFavoriteFragment;

public class MainViewPagerAdapter extends FragmentPagerAdapter {
    private static final int PAGE_COUNT = 2;
    private final RecentFavoriteFragment mRecent;
    private final RecentFavoriteFragment mFavorite;
    private final Context mContext;

    public MainViewPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mRecent = RecentFavoriteFragment.newInstance(true);
        mFavorite = RecentFavoriteFragment.newInstance(false);
        mContext = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return mRecent;
        }
        return mFavorite;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String name;
        switch (position) {
            case 0:
                name = mContext.getString(R.string.recent);
                break;
            default:
                name = mContext.getString(R.string.favorites);
                break;
        }
        return name;
    }
}
