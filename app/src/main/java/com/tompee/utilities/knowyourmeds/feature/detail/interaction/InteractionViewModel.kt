package com.tompee.utilities.knowyourmeds.feature.detail.interaction

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tompee.utilities.knowyourmeds.R
import com.tompee.utilities.knowyourmeds.feature.common.ListViewModel
import com.tompee.utilities.knowyourmeds.interactor.DetailInteractor
import com.tompee.utilities.knowyourmeds.model.InteractionPair
import com.tompee.utilities.knowyourmeds.model.SchedulerPool
import io.reactivex.Completable
import io.reactivex.rxkotlin.plusAssign

class InteractionViewModel private constructor(detailInteractor: DetailInteractor,
                                               schedulerPool: SchedulerPool,
                                               context: Context) :
        ListViewModel<DetailInteractor, InteractionPair>(detailInteractor, schedulerPool) {

    class Factory(private val detailInteractor: DetailInteractor,
                  private val schedulerPool: SchedulerPool,
                  private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return InteractionViewModel(detailInteractor, schedulerPool, context) as T
        }
    }

    init {
        title.postValue(context.getString(R.string.label_interaction))
    }

    override fun search() {
        super.search()
        subscriptions += Completable.fromAction { searching.postValue(true) }
                .andThen(interactor.getInteractions())
                .doOnSuccess { count.postValue(it.size) }
                .doFinally { searching.postValue(false) }
                .subscribeOn(schedulerPool.io)
                .subscribe({
                    isListEmpty.postValue(it.isEmpty())
                    list.postValue(it)
                }) {
                    isListEmpty.postValue(true)
                }
    }
}