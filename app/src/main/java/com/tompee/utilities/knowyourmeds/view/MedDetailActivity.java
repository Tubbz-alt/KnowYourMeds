package com.tompee.utilities.knowyourmeds.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
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
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.tompee.utilities.knowyourmeds.R;
import com.tompee.utilities.knowyourmeds.controller.PauseableHandler;
import com.tompee.utilities.knowyourmeds.controller.database.DatabaseHelper;
import com.tompee.utilities.knowyourmeds.controller.task.GetMedDetailTask;
import com.tompee.utilities.knowyourmeds.model.Medicine;
import com.tompee.utilities.knowyourmeds.view.adapter.DrawerAdapter;
import com.tompee.utilities.knowyourmeds.view.base.BaseActivity;
import com.tompee.utilities.knowyourmeds.view.dialog.ProcessingDialog;
import com.tompee.utilities.knowyourmeds.view.fragment.PropertiesFragment;
import com.tompee.utilities.knowyourmeds.view.fragment.SettingsFragment;
import com.tompee.utilities.knowyourmeds.view.fragment.SourceFragment;
import com.tompee.utilities.knowyourmeds.view.fragment.TtyFragment;
import com.tompee.utilities.knowyourmeds.view.fragment.WebViewFragment;

import java.util.Calendar;
import java.util.Date;

public class MedDetailActivity extends BaseActivity implements GetMedDetailTask.GetMedTaskListener,
        RecyclerView.OnItemTouchListener, PauseableHandler.PauseableHandlerCallback {
    public static final String TAG_NAME = "name";
    public static final String TAG_ORIGIN = "origin";
    public static final int FROM_SEARCH = 0;
    public static final int FROM_RECENTS = 1;
    public static final int FROM_FAVORITES = 2;
    private static final int CACHE_EXPIRY = 12;
    private static final int[] OPTION_IDS = {R.string.tab_properties, R.string.tab_brands,
            R.string.tab_sbdc, R.string.tab_sbd, R.string.tab_sbdg, R.string.tab_scdc,
            R.string.tab_scd, R.string.tab_scdg, R.string.tab_info,
            R.string.tab_sources};
    private RecyclerView mRecyclerView;
    private GestureDetector mGestureDetector;
    private DrawerLayout mDrawer;
    private int mFragmentIndex = 0;
    private GetMedDetailTask mGetMedDetailTask;
    private ProcessingDialog mDialog;
    private Medicine mMedicine;
    private PropertiesFragment mPropertiesFragment;
    private TtyFragment mBrandFragment;
    private TtyFragment mSbdcFragment;
    private TtyFragment mScdcFragment;
    private TtyFragment mSbdgFragment;
    private TtyFragment mScdFragment;
    private TtyFragment mScdgFragment;
    private TtyFragment mSbdFragment;
    private WebViewFragment mWebViewFragment;
    private SourceFragment mSourcesFragment;
    private PauseableHandler mPauseableHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_detail);
        setToolbar(R.id.toolbar, true);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("3AD737A018BB67E7108FD1836E34DD1C").build();
        mAdView.loadAd(adRequest);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mGestureDetector = new GestureDetector(this, new GestureListener());
        mRecyclerView.addOnItemTouchListener(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mPauseableHandler = new PauseableHandler(this);

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
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.SHARED_PREF,
                Context.MODE_PRIVATE);
        if (sharedPreferences.getBoolean(SettingsFragment.TAG_OFFLINE_MODE_CB, false)) {
            DatabaseHelper db = new DatabaseHelper(this);
            mMedicine = db.getEntry(DatabaseHelper.FAVORITE_TABLE, name);
            if (mMedicine == null) {
                mMedicine = db.getEntry(DatabaseHelper.RECENT_TABLE, name);
            }
            initializeDisplay();
        } else {
            int origin = intent.getIntExtra(TAG_ORIGIN, FROM_SEARCH);
            if (origin == FROM_SEARCH) {
                startTask(name);
            } else {
                mMedicine = dbHelper.getEntry(origin == FROM_FAVORITES ? DatabaseHelper.FAVORITE_TABLE :
                        DatabaseHelper.RECENT_TABLE, name);
                if (isCacheValid(mMedicine.getDate(), Calendar.getInstance().getTime())) {
                    initializeDisplay();
                } else {
                    startTask(name);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPauseableHandler.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPauseableHandler.pause();
    }

    private boolean isCacheValid(Date medDate, Date now) {
        long diffHours = (now.getTime() - medDate.getTime()) / (60 * 60 * 1000) % 24;
        return diffHours < CACHE_EXPIRY;
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
        mMedicine = med;
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        int origin = getIntent().getIntExtra(TAG_ORIGIN, FROM_SEARCH);
        switch (origin) {
            case FROM_FAVORITES:
                dbHelper.updateEntry(DatabaseHelper.FAVORITE_TABLE, med);
                break;
            case FROM_RECENTS:
            case FROM_SEARCH:
                dbHelper.createEntry(DatabaseHelper.RECENT_TABLE, med);
                break;
        }
        initializeDisplay();
        mGetMedDetailTask = null;
        mDialog.dismiss();
        mDialog = null;
    }

    @Override
    public void onConnectionError() {
        Toast.makeText(this, getString(R.string.toast_no_internet), Toast.LENGTH_SHORT).show();
        mGetMedDetailTask = null;
        mDialog.dismiss();
        mDialog = null;
        finish();
    }

    private void initializeDisplay() {
        RecyclerView.Adapter adapter = new DrawerAdapter(mMedicine.getName(), OPTION_IDS);
        mRecyclerView.setAdapter(adapter);
        initializeFragments(mMedicine);
        sendMessageToReflectFragment();
    }

    private void initializeFragments(Medicine med) {
        mPropertiesFragment = PropertiesFragment.getInstance();
        mBrandFragment = TtyFragment.newInstance(med.getBrands(), getString(R.string.tab_brands));
        mScdcFragment = TtyFragment.newInstance(med.getScdc(), getString(R.string.tab_scdc));
        mSbdcFragment = TtyFragment.newInstance(med.getSbdc(), getString(R.string.tab_sbdc));
        mSbdgFragment = TtyFragment.newInstance(med.getSbdg(), getString(R.string.tab_sbdg));
        mScdFragment = TtyFragment.newInstance(med.getScd(), getString(R.string.tab_scd));
        mScdgFragment = TtyFragment.newInstance(med.getScdg(), getString(R.string.tab_scdg));
        mSbdFragment = TtyFragment.newInstance(med.getSbd(), getString(R.string.tab_sbd));
        mWebViewFragment = WebViewFragment.getInstance(med.getUrl());
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
        if (mSbdcFragment.isAdded()) {
            transaction.hide(mSbdcFragment);
        }
        if (mScdcFragment.isAdded()) {
            transaction.hide(mScdcFragment);
        }
        if (mSbdgFragment.isAdded()) {
            transaction.hide(mSbdgFragment);
        }
        if (mWebViewFragment.isAdded()) {
            transaction.hide(mWebViewFragment);
        }
        if (mSourcesFragment.isAdded()) {
            transaction.hide(mSourcesFragment);
        }
        if (mScdFragment.isAdded()) {
            transaction.hide(mScdFragment);
        }
        if (mScdgFragment.isAdded()) {
            transaction.hide(mScdgFragment);
        }
        if (mSbdFragment.isAdded()) {
            transaction.hide(mSbdFragment);
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

    private void sendMessageToReflectFragment() {
        Message newMessage = Message.obtain(mPauseableHandler, 0);
        mPauseableHandler.sendMessage(newMessage);
    }

    private void reflectCurrentFragment() {
        hideAllFragments();
        Fragment fragment = null;
        switch (OPTION_IDS[mFragmentIndex]) {
            case R.string.tab_properties:
                fragment = mPropertiesFragment;
                break;
            case R.string.tab_brands:
                fragment = mBrandFragment;
                break;
            case R.string.tab_sbdc:
                fragment = mSbdcFragment;
                break;
            case R.string.tab_scdc:
                fragment = mScdcFragment;
                break;
            case R.string.tab_sbdg:
                fragment = mSbdgFragment;
                break;
            case R.string.tab_info:
                fragment = mWebViewFragment;
                break;
            case R.string.tab_sources:
                fragment = mSourcesFragment;
                break;
            case R.string.tab_scd:
                fragment = mScdFragment;
                break;
            case R.string.tab_scdg:
                fragment = mScdgFragment;
                break;
            case R.string.tab_sbd:
                fragment = mSbdFragment;
                break;
        }
        showFragment(fragment);
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
        View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
        if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {
            int index = recyclerView.getChildLayoutPosition(child) - 1;
            if (index >= 0) {
                mDrawer.closeDrawers();
                mFragmentIndex = index;
                sendMessageToReflectFragment();
                return true;
            }
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }

    @Override
    public boolean storeMessage(Message message) {
        return true;
    }

    @Override
    public void processMessage(Message message) {
        reflectCurrentFragment();
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return true;
        }
    }
}
