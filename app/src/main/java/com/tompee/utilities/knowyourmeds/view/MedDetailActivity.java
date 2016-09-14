package com.tompee.utilities.knowyourmeds.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.tompee.utilities.knowyourmeds.R;
import com.tompee.utilities.knowyourmeds.view.adapter.MedViewPagerAdapter;
import com.tompee.utilities.knowyourmeds.view.base.BaseActivity;
import com.tompee.utilities.knowyourmeds.view.dialog.ProcessingDialog;

public class MedDetailActivity extends BaseActivity {
    public static final String TAG_NAME = "name";
    public static final String TAG_ID = "id";

    private ViewPager mViewPager;

    private ProcessingDialog mDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_detail);
        setToolbar(R.id.toolbar, true);

        Intent intent = getIntent();
        TextView title = (TextView) findViewById(R.id.toolbar_text);
        title.setText(intent.getStringExtra(TAG_NAME));

        mViewPager = (ViewPager) findViewById(R.id.pager_med_detail);
        mViewPager.setAdapter(new MedViewPagerAdapter(this, getSupportFragmentManager(),
                intent.getStringExtra(TAG_ID)));
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout_med_detail);
        tabLayout.setupWithViewPager(mViewPager);

        mDialog = new ProcessingDialog(this, getString(R.string.fetch_details));
//        mDialog.show();
    }

}
