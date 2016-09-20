package com.tompee.utilities.knowyourmeds;

import android.app.Application;

import com.tompee.utilities.knowyourmeds.controller.networkinterface.VolleyRequestQueue;

public class KnowYourMedsApp extends Application {
    public static final String SHARED_PREF_NAME = "know_your_med_shared_prefs";

    @Override
    public void onCreate() {
        super.onCreate();
        /** Initialize volley queue */
        VolleyRequestQueue.getInstance(getApplicationContext());
    }
}
