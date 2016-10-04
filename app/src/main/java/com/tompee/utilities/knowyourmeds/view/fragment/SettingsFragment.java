package com.tompee.utilities.knowyourmeds.view.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tompee.utilities.knowyourmeds.R;
import com.tompee.utilities.knowyourmeds.view.MainActivity;

public class SettingsFragment extends Fragment implements View.OnClickListener {
    public static final String TAG_RECENT_CB = "recent_cb";
    public static final String TAG_OFFLINE_MODE_CB = "offline_cb";
    public static final String TAG_SPL_CB = "spl_cb";
    private SharedPreferences mSharedPreferences;
    private AppCompatCheckBox mRecentCheckBox;
    private AppCompatCheckBox mOfflineModeCheckBox;
    private AppCompatCheckBox mSplCheckBox;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPreferences = getContext().getSharedPreferences(MainActivity.SHARED_PREF,
                Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        mOfflineModeCheckBox = (AppCompatCheckBox) view.findViewById(R.id.offline_mode_cb);
        mOfflineModeCheckBox.setChecked(mSharedPreferences.getBoolean(TAG_OFFLINE_MODE_CB, false));
        View rowView = view.findViewById(R.id.offline_mode);
        rowView.setOnClickListener(this);

        mSplCheckBox = (AppCompatCheckBox) view.findViewById(R.id.spl_support_cb);
        mSplCheckBox.setChecked(mSharedPreferences.getBoolean(TAG_SPL_CB, true));
        rowView = view.findViewById(R.id.spl_support);
        rowView.setOnClickListener(this);

        mRecentCheckBox = (AppCompatCheckBox) view.findViewById(R.id.recent_cb);
        mRecentCheckBox.setChecked(mSharedPreferences.getBoolean(TAG_RECENT_CB, true));
        rowView = view.findViewById(R.id.recent_visibility);
        rowView.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        AlertDialog.Builder builder;
        switch (view.getId()) {
            case R.id.offline_mode:
                builder = new AlertDialog.Builder(getContext());
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
                builder = new AlertDialog.Builder(getContext());
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
            case R.id.recent_visibility:
                boolean isChecked = !mRecentCheckBox.isChecked();
                mRecentCheckBox.setChecked(isChecked);
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putBoolean(TAG_RECENT_CB, isChecked);
                editor.apply();
                break;
        }
    }
}
