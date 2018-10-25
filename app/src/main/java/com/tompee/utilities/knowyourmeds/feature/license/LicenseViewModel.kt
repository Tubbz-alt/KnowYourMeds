package com.tompee.utilities.knowyourmeds.feature.license

import android.content.Context
import android.text.Spanned
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tompee.utilities.knowyourmeds.Constants
import com.tompee.utilities.knowyourmeds.R
import com.tompee.utilities.knowyourmeds.base.BaseViewModel
import com.tompee.utilities.knowyourmeds.interactor.HelpInteractor
import com.tompee.utilities.knowyourmeds.model.SchedulerPool
import io.reactivex.rxkotlin.plusAssign

class LicenseViewModel private constructor(helpInteractor: HelpInteractor,
                                           schedulerPool: SchedulerPool,
                                           private val context: Context) :
        BaseViewModel<HelpInteractor>(helpInteractor, schedulerPool) {

    class Factory(private val helpInteractor: HelpInteractor,
                  private val schedulerPool: SchedulerPool,
                  private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return LicenseViewModel(helpInteractor, schedulerPool, context) as T
        }
    }

    val title = MutableLiveData<String>()
    val message = MutableLiveData<Spanned>()

    fun onLoaded(isLicense: Boolean) {
        subscriptions += if (isLicense) {
            title.postValue(context.getString(R.string.label_license))
            interactor.getStringFromAsset(Constants.LICENSE_ASSET)
                    .subscribeOn(schedulerPool.io)
                    .subscribe(message::postValue)
        } else {
            title.postValue(context.getString(R.string.label_policy))
            interactor.getStringFromAsset(Constants.POLICY_ASSET)
                    .subscribeOn(schedulerPool.io)
                    .subscribe(message::postValue)
        }
    }
}