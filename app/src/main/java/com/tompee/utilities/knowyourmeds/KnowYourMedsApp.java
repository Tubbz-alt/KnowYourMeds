package com.tompee.utilities.knowyourmeds;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.ads.MobileAds;
import com.tompee.utilities.knowyourmeds.controller.networkinterface.VolleyRequestQueue;
import com.tompee.utilities.knowyourmeds.view.MainActivity;

public class KnowYourMedsApp extends Application {
    private static final String TAG_VERSION = "version";
    @Override
    public void onCreate() {
        super.onCreate();
        /** Initialize volley queue */
        VolleyRequestQueue.getInstance(getApplicationContext());
        MobileAds.initialize(getApplicationContext(), getString(R.string.admob_app_id));
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.SHARED_PREF,
                Context.MODE_PRIVATE);
        int version = sharedPreferences.getInt(TAG_VERSION, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (version < BuildConfig.VERSION_CODE) {
            editor.putBoolean(MainActivity.TAG_DISCLAIMER, false);
        }
        editor.putInt(TAG_VERSION, BuildConfig.VERSION_CODE);
        editor.apply();
    }
}
