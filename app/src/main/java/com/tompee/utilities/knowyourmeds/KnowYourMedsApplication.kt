package com.tompee.utilities.knowyourmeds

import android.app.Activity
import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.facebook.stetho.Stetho
import com.google.android.gms.ads.MobileAds
import com.tompee.utilities.knowyourmeds.core.preferences.Preferences
import com.tompee.utilities.knowyourmeds.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class KnowYourMedsApplication : MultiDexApplication(), HasActivityInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var preferences: Preferences

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this);
        MobileAds.initialize(applicationContext, getString(R.string.admob_app_id))

        DaggerAppComponent.builder()
                .application(this)
                .build()
                .inject(this)

        if (preferences.getBuildVersion() < BuildConfig.VERSION_CODE) {
            preferences.showDisclaimerNext(true)
        }
        preferences.setBuildVersion(BuildConfig.VERSION_CODE)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> = dispatchingAndroidInjector
}