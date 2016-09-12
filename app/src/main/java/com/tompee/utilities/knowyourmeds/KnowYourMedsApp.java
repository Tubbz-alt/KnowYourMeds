package com.tompee.utilities.knowyourmeds;

import android.app.Application;

import com.tompee.utilities.knowyourmeds.controller.networkinterface.VolleyRequestQueue;

public class KnowYourMedsApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        /** Initialize volley queue */
        VolleyRequestQueue.getInstance(getApplicationContext());
    }
}
