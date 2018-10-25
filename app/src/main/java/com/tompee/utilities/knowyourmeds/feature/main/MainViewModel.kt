package com.tompee.utilities.knowyourmeds.feature.main

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tompee.utilities.knowyourmeds.Constants
import com.tompee.utilities.knowyourmeds.R
import com.tompee.utilities.knowyourmeds.base.BaseViewModel
import com.tompee.utilities.knowyourmeds.core.asset.AssetManager
import com.tompee.utilities.knowyourmeds.extensions.default
import com.tompee.utilities.knowyourmeds.interactor.SearchInteractor
import com.tompee.utilities.knowyourmeds.model.SchedulerPool
import io.reactivex.rxkotlin.plusAssign

class MainViewModel private constructor(searchInteractor: SearchInteractor,
                                        schedulerPool: SchedulerPool,
                                        assetManager: AssetManager,
                                        context: Context) :
        BaseViewModel<SearchInteractor>(searchInteractor, schedulerPool) {

    class Factory(private val searchInteractor: SearchInteractor,
                  private val schedulerPool: SchedulerPool,
                  private val assetManager: AssetManager,
                  private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(searchInteractor, schedulerPool, assetManager, context) as T
        }
    }

    val isShowDisclaimer = MutableLiveData<Boolean>().default(false)
    val isShowAppRater = MutableLiveData<Boolean>().default(false)
    val title = MutableLiveData<String>()
    val background = MutableLiveData<Drawable>()
    val needsClose = MutableLiveData<Boolean>().default(false)

    init {
        title.postValue(context.getString(R.string.app_name))
        background.postValue(assetManager.getDrawableFromAsset(Constants.BACKGROUND_ASSET))

        subscriptions += interactor.getIsDisplayDisclaimer()
                .subscribe(isShowDisclaimer::postValue)

        subscriptions += interactor.getIsDueForRater()
                .subscribe(isShowAppRater::postValue)
    }

    fun onDisclaimerCancelled() {
        needsClose.postValue(true)
    }

    fun onDisclaimerAccepted() {
        interactor.setShowDisclaimerNext(false)
    }
}