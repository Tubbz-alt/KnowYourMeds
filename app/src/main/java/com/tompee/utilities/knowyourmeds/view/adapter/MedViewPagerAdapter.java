package com.tompee.utilities.knowyourmeds.view.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tompee.utilities.knowyourmeds.R;
import com.tompee.utilities.knowyourmeds.view.fragment.PropertiesFragment;
import com.tompee.utilities.knowyourmeds.view.fragment.RecentFavoriteFragment;
import com.tompee.utilities.knowyourmeds.view.fragment.SearchFragment;

public class MedViewPagerAdapter extends FragmentPagerAdapter {
    private static final int PAGE_COUNT = 1;
    private static final int[] mStringNameIds = {R.string.properties, R.string.ingredients,
            R.string.properties};

    private final Context mContext;
    private final PropertiesFragment mPropertiesFragment;

    public MedViewPagerAdapter(Context context, FragmentManager fm, String rxcui) {
        super(fm);
        mContext = context;
        mPropertiesFragment = PropertiesFragment.newInstance(rxcui);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        return mPropertiesFragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getString(mStringNameIds[position]);
    }
}
