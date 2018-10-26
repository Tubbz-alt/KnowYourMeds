package com.tompee.utilities.knowyourmeds.feature.main.cache

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tompee.utilities.knowyourmeds.R
import com.tompee.utilities.knowyourmeds.feature.common.ListViewModel
import com.tompee.utilities.knowyourmeds.interactor.SearchInteractor
import com.tompee.utilities.knowyourmeds.model.Medicine
import com.tompee.utilities.knowyourmeds.model.SchedulerPool
import io.reactivex.Completable
import io.reactivex.rxkotlin.plusAssign

class CacheViewModel private constructor(searchInteractor: SearchInteractor,
                                         schedulerPool: SchedulerPool,
                                         private val context: Context) :
        ListViewModel<SearchInteractor, Medicine>(searchInteractor, schedulerPool) {

    class Factory(private val searchInteractor: SearchInteractor,
                  private val schedulerPool: SchedulerPool,
                  private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return CacheViewModel(searchInteractor, schedulerPool, context) as T
        }
    }

    fun onLoad(isRecent: Boolean) {
        title.postValue(context.getString(if (isRecent) R.string.label_recent else R.string.label_favorites))
        if (isRecent) {
            subscriptions += Completable.fromAction {
                showSearchButton.postValue(false)
                searching.postValue(false)
            }.andThen(interactor.getMedicineList())
                    .subscribeOn(schedulerPool.io)
                    .subscribe(::postList, { Log.d("room", it.message) }) { Log.d("room", "i was terminated") }
        }
    }

    fun delete(isRecent: Boolean, medicine: Medicine) {
        if (isRecent) {
            interactor.deleteMedicine(medicine)
                    .subscribeOn(schedulerPool.io)
                    .subscribe()
        }
    }

    fun searchNew(medicine: Medicine) {
        interactor.setNewStockMedicine(medicine)
    }
}