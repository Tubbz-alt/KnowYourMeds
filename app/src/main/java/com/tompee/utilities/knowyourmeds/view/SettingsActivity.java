package com.tompee.utilities.knowyourmeds.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tompee.utilities.knowyourmeds.R;
import com.tompee.utilities.knowyourmeds.controller.Utilities;
import com.tompee.utilities.knowyourmeds.controller.database.DatabaseHelper;
import com.tompee.utilities.knowyourmeds.view.base.BaseActivity;

public class SettingsActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG_OFFLINE_MODE_CB = "offline_cb";
    public static final String TAG_SPL_CB = "spl_cb";
    private SharedPreferences mSharedPreferences;
    private AppCompatCheckBox mOfflineModeCheckBox;
    private AppCompatCheckBox mSplCheckBox;

    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setToolbar(R.id.toolbar, true);

        TextView title = (TextView) findViewById(R.id.toolbar_text);
        title.setText(R.string.settings);

        ImageView background = (ImageView) findViewById(R.id.background);
        background.setImageDrawable(Utilities.getDrawableFromAsset(this, "search_bg.jpg"));

        mSharedPreferences = getSharedPreferences(MainActivity.SHARED_PREF,
                Context.MODE_PRIVATE);
        mOfflineModeCheckBox = (AppCompatCheckBox) findViewById(R.id.offline_mode_cb);
        mOfflineModeCheckBox.setChecked(mSharedPreferences.getBoolean(TAG_OFFLINE_MODE_CB, false));
        View rowView = findViewById(R.id.offline_mode);
        rowView.setOnClickListener(this);

        mSplCheckBox = (AppCompatCheckBox) findViewById(R.id.spl_support_cb);
        mSplCheckBox.setChecked(mSharedPreferences.getBoolean(TAG_SPL_CB, true));
        rowView = findViewById(R.id.spl_support);
        rowView.setOnClickListener(this);

        rowView = findViewById(R.id.cache);
        rowView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        AlertDialog.Builder builder;
        switch (view.getId()) {
            case R.id.offline_mode:
                builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.settings_offline_mode_title);
                builder.setMessage(R.string.settings_offline_mode_message);
                builder.setPositiveButton(R.string.control_enable, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mOfflineModeCheckBox.setChecked(true);
                        SharedPreferences.Editor editor = mSharedPreferences.edit();
                        editor.putBoolean(TAG_OFFLINE_MODE_CB, true);
                        editor.apply();
                    }
                });
                builder.setNegativeButton(R.string.control_disable, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mOfflineModeCheckBox.setChecked(false);
                        SharedPreferences.Editor editor = mSharedPreferences.edit();
                        editor.putBoolean(TAG_OFFLINE_MODE_CB, false);
                        editor.apply();
                    }
                });
                builder.setCancelable(false);
                builder.create().show();
                break;
            case R.id.spl_support:
                builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.settings_spl_support_title);
                builder.setMessage(R.string.settings_spl_support_message);
                builder.setPositiveButton(R.string.control_enable, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mSplCheckBox.setChecked(true);
                        SharedPreferences.Editor editor = mSharedPreferences.edit();
                        editor.putBoolean(TAG_SPL_CB, true);
                        editor.apply();
                    }
                });
                builder.setNegativeButton(R.string.control_disable, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mSplCheckBox.setChecked(false);
                        SharedPreferences.Editor editor = mSharedPreferences.edit();
                        editor.putBoolean(TAG_SPL_CB, false);
                        editor.apply();
                    }
                });
                builder.setCancelable(false);
                builder.create().show();
                break;
            case R.id.cache:
                builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.settings_cache_title);
                builder.setMessage(R.string.settings_cache_message);
                builder.setPositiveButton(R.string.control_empty_cache, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatabaseHelper db = DatabaseHelper.getInstance(SettingsActivity.this);
                        db.deleteAll();
                    }
                });
                builder.setNegativeButton(R.string.control_cancel, null);
                builder.setCancelable(false);
                builder.create().show();
                break;
        }
    }
}
