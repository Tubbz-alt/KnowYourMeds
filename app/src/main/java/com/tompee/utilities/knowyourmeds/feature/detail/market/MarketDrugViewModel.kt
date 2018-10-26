package com.tompee.utilities.knowyourmeds.feature.detail.market

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tompee.utilities.knowyourmeds.R
import com.tompee.utilities.knowyourmeds.feature.common.ListViewModel
import com.tompee.utilities.knowyourmeds.interactor.DetailInteractor
import com.tompee.utilities.knowyourmeds.model.MarketDrug
import com.tompee.utilities.knowyourmeds.model.SchedulerPool
import io.reactivex.Completable
import io.reactivex.rxkotlin.plusAssign

class MarketDrugViewModel private constructor(detailInteractor: DetailInteractor,
                                              schedulerPool: SchedulerPool,
                                              context: Context) :
        ListViewModel<DetailInteractor, MarketDrug>(detailInteractor, schedulerPool) {

    class Factory(private val detailInteractor: DetailInteractor,
                  private val schedulerPool: SchedulerPool,
                  private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return MarketDrugViewModel(detailInteractor, schedulerPool, context) as T
        }
    }

    init {
        title.postValue(context.getString(R.string.label_market_drugs))
        subscriptions += Completable.fromAction {
            showSearchButton.postValue(false)
            searching.postValue(true)
        }.andThen(interactor.getCachedMarketDrugs())
                .doFinally { searching.postValue(false) }
                .subscribeOn(schedulerPool.io)
                .subscribe(::postList) {
                    showSearchButton.postValue(true)
                }
    }

    override fun search() {
        super.search()
        subscriptions += Completable.fromAction { searching.postValue(true) }
                .andThen(interactor.getMarketDrugs())
                .doFinally { searching.postValue(false) }
                .subscribeOn(schedulerPool.io)
                .subscribe(::postList) { isListEmpty.postValue(true) }
    }
}