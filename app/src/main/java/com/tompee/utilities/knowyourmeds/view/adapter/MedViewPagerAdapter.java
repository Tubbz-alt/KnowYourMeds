package com.tompee.utilities.knowyourmeds.view.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tompee.utilities.knowyourmeds.R;
import com.tompee.utilities.knowyourmeds.view.fragment.PropertiesFragment;
import com.tompee.utilities.knowyourmeds.view.fragment.SearchFragment;

public class MedViewPagerAdapter extends FragmentPagerAdapter {
    private static final int PAGE_COUNT = 1;
    private static final int[] mStringNameIds = {R.string.tab_properties, R.string.tab_ingredients,
            R.string.tab_properties};

    private final Context mContext;
    private final PropertiesFragment mPropertiesFragment;

    public MedViewPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
        mPropertiesFragment = PropertiesFragment.newInstance();
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
