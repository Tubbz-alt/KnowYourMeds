package com.tompee.utilities.knowyourmeds.view.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.tompee.utilities.knowyourmeds.R;
import com.tompee.utilities.knowyourmeds.view.MainActivity;

public class SettingsFragment extends Fragment {
    public static final String TAG_RECENT_CB = "recent_cb";
    private SharedPreferences mSharedPreferences;

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
        final AppCompatCheckBox recentCheckBox = (AppCompatCheckBox) view.findViewById(R.id.recent_cb);
        recentCheckBox.setChecked(mSharedPreferences.getBoolean(TAG_RECENT_CB, true));
        View recentVisibility = view.findViewById(R.id.recent_visibility);
        recentVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = !recentCheckBox.isChecked();
                recentCheckBox.setChecked(isChecked);
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putBoolean(TAG_RECENT_CB, isChecked);
                editor.apply();
            }
        });
        return view;
    }
}
