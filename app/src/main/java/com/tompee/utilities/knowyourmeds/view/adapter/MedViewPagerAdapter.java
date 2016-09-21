package com.tompee.utilities.knowyourmeds.view.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tompee.utilities.knowyourmeds.R;
import com.tompee.utilities.knowyourmeds.view.fragment.BrandFragment;
import com.tompee.utilities.knowyourmeds.view.fragment.PropertiesFragment;
import com.tompee.utilities.knowyourmeds.view.fragment.WebViewFragment;

public class MedViewPagerAdapter extends FragmentPagerAdapter {
    private static final int PAGE_COUNT = 3;
    private static final int[] mStringNameIds = {R.string.tab_properties, R.string.tab_brands,
            R.string.tab_info};
    private final Context mContext;
    private final PropertiesFragment mPropertiesFragment;
    private final BrandFragment mBrandFragment;
    private final WebViewFragment mWebViewFragment;

    public MedViewPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
        mPropertiesFragment = PropertiesFragment.newInstance();
        mBrandFragment = BrandFragment.newInstance();
        mWebViewFragment = WebViewFragment.newInstance();
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return mPropertiesFragment;
            case 1:
                return mBrandFragment;
            case 2:
                return mWebViewFragment;
            default:
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getString(mStringNameIds[position]);
    }
}
