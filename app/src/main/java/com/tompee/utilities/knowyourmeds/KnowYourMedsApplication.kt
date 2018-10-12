package com.tompee.utilities.knowyourmeds

import android.content.Context
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import com.google.android.gms.ads.MobileAds
import com.tompee.utilities.knowyourmeds.di.component.AppComponent
import com.tompee.utilities.knowyourmeds.di.component.DaggerAppComponent
import com.tompee.utilities.knowyourmeds.di.module.AppModule

class KnowYourMedsApplication : MultiDexApplication() {

    companion object {
        operator fun get(context: Context): KnowYourMedsApplication {
            return context.applicationContext as KnowYourMedsApplication
        }
    }

    lateinit var component: AppComponent

    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(applicationContext, getString(R.string.admob_app_id))

        component = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
        val preferences = component.preferences()
        if (preferences.getBuildVersion() < BuildConfig.VERSION_CODE) {
            preferences.showDisclaimerNext()
        }
        preferences.setBuildVersion(BuildConfig.VERSION_CODE)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}