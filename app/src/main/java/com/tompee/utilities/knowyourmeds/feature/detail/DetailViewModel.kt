package com.tompee.utilities.knowyourmeds.feature.detail

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
import com.tompee.utilities.knowyourmeds.feature.detail.property.PropertyFragment
import com.tompee.utilities.knowyourmeds.interactor.DetailInteractor
import com.tompee.utilities.knowyourmeds.model.SchedulerPool
import io.reactivex.Completable
import io.reactivex.rxkotlin.plusAssign

class DetailViewModel private constructor(detailInteractor: DetailInteractor,
                                          schedulerPool: SchedulerPool,
                                          assetManager: AssetManager,
                                          context: Context) :
        BaseViewModel<DetailInteractor>(detailInteractor, schedulerPool) {

    class Factory(private val detailInteractor: DetailInteractor,
                  private val schedulerPool: SchedulerPool,
                  private val assetManager: AssetManager,
                  private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return DetailViewModel(detailInteractor, schedulerPool, assetManager, context) as T
        }
    }

    val title = MutableLiveData<String>()
    val message = MutableLiveData<String>()
    val background = MutableLiveData<Drawable>()
    val searching = MutableLiveData<Boolean>().default(false)
    val propertyFragment = MutableLiveData<PropertyFragment>()

    init {
        background.postValue(assetManager.getDrawableFromAsset(Constants.BACKGROUND_ASSET))
        message.postValue(context.getString(R.string.message_fetch_details))

        subscriptions += interactor.getStockMedicine()
                .map { it.name }
                .subscribe(title::postValue)

        subscriptions += Completable.fromAction { searching.postValue(true) }
                .andThen(interactor.getDetailedInfo())
                .doOnSuccess { propertyFragment.postValue(PropertyFragment()) }
                .subscribeOn(schedulerPool.io)
                .subscribe { _ -> searching.postValue(false) }
    }
}