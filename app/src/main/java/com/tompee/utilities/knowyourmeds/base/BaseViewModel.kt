package com.tompee.utilities.knowyourmeds.base

import androidx.lifecycle.ViewModel
import com.tompee.utilities.knowyourmeds.model.SchedulerPool
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel<T : BaseInteractor>(protected val interactor: T,
                                                 protected val schedulerPool: SchedulerPool) : ViewModel() {
    protected val subscriptions = CompositeDisposable()

    override fun onCleared() {
        subscriptions.clear()
        super.onCleared()
    }
}