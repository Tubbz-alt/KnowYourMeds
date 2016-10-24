package com.tompee.utilities.knowyourmeds.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tompee.utilities.knowyourmeds.R;
import com.tompee.utilities.knowyourmeds.controller.PauseableHandler;
import com.tompee.utilities.knowyourmeds.controller.Utilities;
import com.tompee.utilities.knowyourmeds.controller.database.DatabaseHelper;
import com.tompee.utilities.knowyourmeds.controller.networkinterface.RxNavWrapper;
import com.tompee.utilities.knowyourmeds.controller.task.GetMedDetailTask;
import com.tompee.utilities.knowyourmeds.model.Medicine;
import com.tompee.utilities.knowyourmeds.view.base.BaseActivity;
import com.tompee.utilities.knowyourmeds.view.dialog.MenuDialog;
import com.tompee.utilities.knowyourmeds.view.dialog.ProcessingDialog;
import com.tompee.utilities.knowyourmeds.view.fragment.InteractionFragment;
import com.tompee.utilities.knowyourmeds.view.fragment.PropertiesFragment;
import com.tompee.utilities.knowyourmeds.view.fragment.SourceFragment;
import com.tompee.utilities.knowyourmeds.view.fragment.TtyFragment;
import com.tompee.utilities.knowyourmeds.view.fragment.WebViewFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MedDetailActivity extends BaseActivity implements GetMedDetailTask.GetMedTaskListener,
        PauseableHandler.PauseableHandlerCallback, View.OnClickListener, MenuDialog.MenuDialogListener {
    public static final String TAG_NAME = "name";
    public static final String TAG_ID = "id";
    public static final String TAG_ORIGIN = "origin";
    public static final int FROM_SEARCH = 0;
    public static final int FROM_RECENTS = 1;
    public static final int FROM_FAVORITES = 2;
    private static final String POSITION = "position";
    private static final int CACHE_EXPIRY = 12;
    private GetMedDetailTask mGetMedDetailTask;
    private ProcessingDialog mDialog;
    private Medicine mMedicine;
    private List<Fragment> mFragmentList;
    private PauseableHandler mPauseableHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_detail);
        setToolbar(R.id.toolbar, true);

        Intent intent = getIntent();
        String name = intent.getStringExtra(TAG_NAME);
        TextView title = (TextView) findViewById(R.id.toolbar_text);
        title.setText(name);

        ImageView imageView = (ImageView) findViewById(R.id.background);
        imageView.setImageDrawable(Utilities.getDrawableFromAsset(this, "search_bg.jpg"));

        FloatingActionButton menu = (FloatingActionButton) findViewById(R.id.menu);
        menu.setOnClickListener(this);

        mPauseableHandler = new PauseableHandler(this);
        mFragmentList = new ArrayList<>();

        /** Regardless of origin, search database first */
        DatabaseHelper db = DatabaseHelper.getInstance(this);
        SharedPreferences sp = getSharedPreferences(MainActivity.SHARED_PREF,
                Context.MODE_PRIVATE);

        mMedicine = db.getEntry(getIntent().getStringExtra(TAG_ID));
        /** Check if offline mode is enabled */
        if (sp.getBoolean(SettingsActivity.TAG_OFFLINE_MODE_CB, false)) {
            if (mMedicine == null) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                dialogBuilder.setMessage(R.string.not_available_offline);
                dialogBuilder.setPositiveButton(R.string.control_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                dialogBuilder.create().show();
                return;
            }
        } else {
            if (mMedicine == null || !isCacheValid(mMedicine.getDate(),
                    Calendar.getInstance().getTime())) {
                startTask(name);
                return;
            }
        }
        if (getIntent().getIntExtra(TAG_ORIGIN, FROM_SEARCH) == FROM_SEARCH) {
            db.createEntry(DatabaseHelper.RECENT_TABLE, mMedicine);
        }
        initializeDisplay();
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
        MenuItem favorite = menu.findItem(R.id.menu_favorite);
        if (mMedicine != null) {
            DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
            Medicine med = dbHelper.getShortEntry(DatabaseHelper.FAVORITE_TABLE,
                    mMedicine.getRxnormId());
            if (med != null) {
                favorite.setIcon(R.drawable.ic_star_white);
            } else {
                favorite.setIcon(R.drawable.ic_star_border_white);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_favorite) {
            DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
            if (dbHelper.getShortEntry(DatabaseHelper.FAVORITE_TABLE, mMedicine.getRxnormId()) != null) {
                dbHelper.deleteEntry(DatabaseHelper.FAVORITE_TABLE, mMedicine);
            } else {
                dbHelper.createEntry(DatabaseHelper.FAVORITE_TABLE, mMedicine);
            }
        }
        invalidateOptionsMenu();
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
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
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
        invalidateOptionsMenu();
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
        initializeFragments(mMedicine);
        sendMessageToReflectFragment(0);
    }

    private void initializeFragments(Medicine med) {
        Fragment fragment = PropertiesFragment.getInstance();
        mFragmentList.add(fragment);
        if (med.getBrands() != null && !med.getBrands().isEmpty()) {
            fragment = TtyFragment.newInstance(med.getBrands(), getString(R.string.tab_brands), RxNavWrapper.BRAND);
            mFragmentList.add(fragment);
        }
        if (med.getSbdc() != null && !med.getSbdc().isEmpty()) {
            fragment = TtyFragment.newInstance(med.getSbdc(), getString(R.string.tab_sbdc),
                    RxNavWrapper.SBDC);
            mFragmentList.add(fragment);
        }
        if (med.getSbd() != null && !med.getSbd().isEmpty()) {
            fragment = TtyFragment.newInstance(med.getSbd(), getString(R.string.tab_sbd),
                    RxNavWrapper.SBD);
            mFragmentList.add(fragment);
        }
        if (med.getSbdg() != null && !med.getSbdg().isEmpty()) {
            fragment = TtyFragment.newInstance(med.getSbdg(), getString(R.string.tab_sbdg),
                    RxNavWrapper.SBDG);
            mFragmentList.add(fragment);
        }
        if (med.getScdc() != null && !med.getScdc().isEmpty()) {
            fragment = TtyFragment.newInstance(med.getScdc(), getString(R.string.tab_scdc),
                    RxNavWrapper.SCDC);
            mFragmentList.add(fragment);
        }
        if (med.getScd() != null && !med.getScd().isEmpty()) {
            fragment = TtyFragment.newInstance(med.getScd(), getString(R.string.tab_scd),
                    RxNavWrapper.SCD);
            mFragmentList.add(fragment);
        }
        if (med.getScdg() != null && !med.getScdg().isEmpty()) {
            fragment = TtyFragment.newInstance(med.getScdg(), getString(R.string.tab_scdg),
                    RxNavWrapper.SCDG);
            mFragmentList.add(fragment);
        }
        if (med.getUrl() != null) {
            fragment = WebViewFragment.newInstance(med.getUrl());
            mFragmentList.add(fragment);
        }
        if (med.getInteractions() != null && !med.getInteractions().isEmpty()) {
            fragment = InteractionFragment.newInstance();
            mFragmentList.add(fragment);
        }
        if (med.getSources() != null && !med.getSources().isEmpty()) {
            fragment = SourceFragment.getInstance();
            mFragmentList.add(fragment);
        }
    }

    private void hideAllFragments() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        for (Fragment fragment : mFragmentList) {
            if (fragment.isAdded()) {
                transaction.hide(fragment);
            }
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

    private void sendMessageToReflectFragment(int position) {
        Message newMessage = Message.obtain(mPauseableHandler, 0);
        Bundle bundle = new Bundle();
        bundle.putInt(POSITION, position);
        newMessage.setData(bundle);
        mPauseableHandler.sendMessage(newMessage);
    }

    private void reflectCurrentFragment(int index) {
        hideAllFragments();
        Fragment fragment = mFragmentList.get(index);
        showFragment(fragment);
    }

    @Override
    public boolean storeMessage(Message message) {
        return true;
    }

    @Override
    public void processMessage(Message message) {
        reflectCurrentFragment(message.getData().getInt(POSITION));
    }

    @Override
    public void onClick(View v) {
        Dialog dialog = new MenuDialog(this, mMedicine, this);
        dialog.show();
    }

    @Override
    public void onMenuClicked(int position) {
        sendMessageToReflectFragment(position);
    }
}
