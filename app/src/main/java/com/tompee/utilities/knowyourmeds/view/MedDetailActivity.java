package com.tompee.utilities.knowyourmeds.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.tompee.utilities.knowyourmeds.R;
import com.tompee.utilities.knowyourmeds.controller.database.DatabaseHelper;
import com.tompee.utilities.knowyourmeds.controller.task.GetMedDetailTask;
import com.tompee.utilities.knowyourmeds.model.Medicine;
import com.tompee.utilities.knowyourmeds.view.adapter.DrawerAdapter;
import com.tompee.utilities.knowyourmeds.view.base.BaseActivity;
import com.tompee.utilities.knowyourmeds.view.dialog.ProcessingDialog;
import com.tompee.utilities.knowyourmeds.view.fragment.BrandFragment;
import com.tompee.utilities.knowyourmeds.view.fragment.PropertiesFragment;
import com.tompee.utilities.knowyourmeds.view.fragment.SbdcFragment;
import com.tompee.utilities.knowyourmeds.view.fragment.ScdcFragment;
import com.tompee.utilities.knowyourmeds.view.fragment.SourceFragment;
import com.tompee.utilities.knowyourmeds.view.fragment.WebViewFragment;

public class MedDetailActivity extends BaseActivity implements GetMedDetailTask.GetMedTaskListener,
        RecyclerView.OnItemTouchListener {
    public static final String TAG_NAME = "name";
    private static final int[] OPTION_IDS = {R.string.tab_properties, R.string.tab_brands,
            R.string.tab_scdc, R.string.tab_sbdc, R.string.tab_info, R.string.tab_sources};

    private RecyclerView mRecyclerView;
    private GestureDetector mGestureDetector;
    private DrawerLayout mDrawer;
    private int mFragmentIndex = 0;

    private GetMedDetailTask mGetMedDetailTask;
    private ProcessingDialog mDialog;
    private Medicine mMedicine;

    private PropertiesFragment mPropertiesFragment;
    private BrandFragment mBrandFragment;
    private ScdcFragment mScdcFragment;
    private SbdcFragment mSbdcFragment;
    private WebViewFragment mWebViewFragment;
    private SourceFragment mSourcesFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_detail);
        setToolbar(R.id.toolbar, true);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mGestureDetector = new GestureDetector(this, new GestureListener());
        mRecyclerView.addOnItemTouchListener(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        //Set drawer
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawer,
                (Toolbar) findViewById(R.id.toolbar), R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        Intent intent = getIntent();
        String name = intent.getStringExtra(TAG_NAME);
        TextView title = (TextView) findViewById(R.id.toolbar_text);
        title.setText(name);
        startTask(name);
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
        RecyclerView.Adapter adapter = new DrawerAdapter(OPTION_IDS);
        mRecyclerView.setAdapter(adapter);
        initializeFragments();
        reflectCurrentFragment();
    }

    private void initializeFragments() {
        mPropertiesFragment = PropertiesFragment.getInstance();
        mBrandFragment = BrandFragment.getInstance();
        mScdcFragment = ScdcFragment.getInstance();
        mSbdcFragment = SbdcFragment.getInstance();
        mWebViewFragment = WebViewFragment.getInstance();
        mSourcesFragment = SourceFragment.getInstance();
    }

    private void hideAllFragments() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (mPropertiesFragment.isAdded()) {
            transaction.hide(mPropertiesFragment);
        }
        if (mBrandFragment.isAdded()) {
            transaction.hide(mBrandFragment);
        }
        if (mScdcFragment.isAdded()) {
            transaction.hide(mScdcFragment);
        }
        if (mSbdcFragment.isAdded()) {
            transaction.hide(mSbdcFragment);
        }
        if (mWebViewFragment.isAdded()) {
            transaction.hide(mWebViewFragment);
        }
        if (mSourcesFragment.isAdded()) {
            transaction.hide(mSourcesFragment);
        }
        transaction.commit();
        fragmentManager.executePendingTransactions();
    }

    private void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (fragment.isAdded()) {
            transaction.show(fragment);
        } else {
            transaction.add(R.id.container, fragment);
        }
        transaction.commit();
        fragmentManager.executePendingTransactions();
    }

    private void reflectCurrentFragment() {
        hideAllFragments();
        switch (mFragmentIndex) {
            case 0:
                showFragment(mPropertiesFragment);
                break;
            case 1:
                showFragment(mBrandFragment);
                break;
            case 2:
                showFragment(mScdcFragment);
                break;
            case 3:
                showFragment(mSbdcFragment);
                break;
            case 4:
                showFragment(mWebViewFragment);
                break;
            case 5:
                showFragment(mSourcesFragment);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
        View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
        if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {
            mDrawer.closeDrawers();
            mFragmentIndex = recyclerView.getChildLayoutPosition(child) - 1;
            reflectCurrentFragment();
            return true;
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return true;
        }
    }
}
