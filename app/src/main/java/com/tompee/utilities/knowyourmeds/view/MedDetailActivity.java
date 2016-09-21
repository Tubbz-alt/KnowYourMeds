package com.tompee.utilities.knowyourmeds.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.tompee.utilities.knowyourmeds.R;
import com.tompee.utilities.knowyourmeds.controller.database.DatabaseHelper;
import com.tompee.utilities.knowyourmeds.controller.task.GetMedDetailTask;
import com.tompee.utilities.knowyourmeds.model.Medicine;
import com.tompee.utilities.knowyourmeds.view.adapter.MedViewPagerAdapter;
import com.tompee.utilities.knowyourmeds.view.base.BaseActivity;
import com.tompee.utilities.knowyourmeds.view.dialog.ProcessingDialog;

public class MedDetailActivity extends BaseActivity implements GetMedDetailTask.GetMedTaskListener,
        ViewPager.OnPageChangeListener {
    public static final String TAG_NAME = "name";

    private ViewPager mViewPager;
    private GetMedDetailTask mGetMedDetailTask;
    private ProcessingDialog mDialog;
    private Medicine mMedicine;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_detail);
        setToolbar(R.id.toolbar, true);

        Intent intent = getIntent();
        String medName = intent.getStringExtra(TAG_NAME);
        TextView title = (TextView) findViewById(R.id.toolbar_text);
        title.setText(medName);

        mViewPager = (ViewPager) findViewById(R.id.pager_med_detail);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setOffscreenPageLimit(3);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout_med_detail);
        tabLayout.setupWithViewPager(mViewPager);

        startTask(medName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_med_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_favorite) {
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            dbHelper.createEntry(DatabaseHelper.FAVORITE_TABLE, mMedicine);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(String.format(getString(R.string.add_to_favorites),
                    mMedicine.getName()));
            builder.setPositiveButton(R.string.control_ok, null);
            builder.create().show();

        }
        return super.onOptionsItemSelected(item);
    }

    private void startTask(String medName) {
        if (mGetMedDetailTask == null) {
            if (mDialog == null) {
                mDialog = new ProcessingDialog(this, getString(R.string.fetch_details));
                mDialog.show();
            }
            mGetMedDetailTask = new GetMedDetailTask(this, this);
            mGetMedDetailTask.execute(medName);
        }
    }

    public Medicine getMedicine() {
        return mMedicine;
    }

    @Override
    public void onCompleted(Medicine med) {
        mGetMedDetailTask = null;
        mDialog.dismiss();
        mDialog = null;
        mMedicine = med;
        mViewPager.setAdapter(new MedViewPagerAdapter(this, getSupportFragmentManager()));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
