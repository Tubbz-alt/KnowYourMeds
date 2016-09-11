package com.tompee.utilities.knowyourmeds.view;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.tompee.utilities.knowyourmeds.R;
import com.tompee.utilities.knowyourmeds.view.adapter.ViewPagerAdapter;
import com.tompee.utilities.knowyourmeds.view.base.BaseActivity;

public class MainActivity extends BaseActivity {
    private ViewPager mViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbar(R.id.toolbar, false);
        TextView title = (TextView) findViewById(R.id.toolbar_text);
        title.setText(R.string.app_name);

        mViewPager = (ViewPager) findViewById(R.id.pager_main);
        mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout_main);
        tabLayout.setupWithViewPager(mViewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            //noinspection ConstantConditions
            tabLayout.getTabAt(i).setIcon(R.drawable.ic_search_white);
        }
    }
}
