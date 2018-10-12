package com.tompee.utilities.knowyourmeds.core.preferences

interface Preferences {
    fun getBuildVersion(): Int
    fun setBuildVersion(version: Int)

    fun showDisclaimerNext()
    fun isShowDisclaimer(): Boolean

    fun isDueForRater(): Boolean
    fun incrementRaterCount()

    fun isSplEnabled() : Boolean
}