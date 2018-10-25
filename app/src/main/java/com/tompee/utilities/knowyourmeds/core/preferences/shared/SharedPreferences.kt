package com.tompee.utilities.knowyourmeds.core.preferences.shared

import android.content.Context
import com.tompee.utilities.knowyourmeds.BuildConfig
import com.tompee.utilities.knowyourmeds.core.preferences.Preferences

class SharedPreferences(context: Context) : Preferences {

    private val sharedPreferences = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)

    companion object {
        private const val TAG_VERSION = "version"
        private const val SHARED_PREF = "knowYourMedsPref"
        private const val TAG_DISCLAIMER = "disclaimer"
        private const val LAUNCH_COUNT = "launch_count"
        private const val TAG_SPL_CB = "spl_cb"
        private const val MIN_LAUNCH_COUNT = 7
    }

    override fun getBuildVersion(): Int {
        return sharedPreferences.getInt(TAG_VERSION, 0)
    }

    override fun setBuildVersion(version: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(TAG_VERSION, BuildConfig.VERSION_CODE)
        editor.apply()
    }

    override fun showDisclaimerNext(isShow : Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(TAG_DISCLAIMER, isShow)
        editor.apply()
    }

    override fun isShowDisclaimer(): Boolean = sharedPreferences.getBoolean(TAG_DISCLAIMER, false)

    override fun isDueForRater(): Boolean {
        val launchCount = sharedPreferences.getInt(LAUNCH_COUNT, 0)
        return launchCount == MIN_LAUNCH_COUNT
    }

    override fun incrementRaterCount() {
        val editor = sharedPreferences.edit()
        var launchCount = sharedPreferences.getInt(LAUNCH_COUNT, 0)
        if (launchCount == MIN_LAUNCH_COUNT) {
            editor.putInt(LAUNCH_COUNT, 0)
        } else {
            editor.putInt(LAUNCH_COUNT, ++launchCount)
        }
        editor.apply()
    }

    override fun isSplEnabled(): Boolean {
        return sharedPreferences.getBoolean(TAG_SPL_CB, true)
    }
}