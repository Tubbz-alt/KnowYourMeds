package com.tompee.utilities.knowyourmeds;

import android.app.Application;

import com.google.android.gms.ads.MobileAds;
import com.tompee.utilities.knowyourmeds.controller.networkinterface.VolleyRequestQueue;

public class KnowYourMedsApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        /** Initialize volley queue */
        VolleyRequestQueue.getInstance(getApplicationContext());
        MobileAds.initialize(getApplicationContext(), getString(R.string.admob_app_id));
    }
}
