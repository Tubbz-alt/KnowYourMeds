package com.tompee.utilities.knowyourmeds.feature.common

import androidx.lifecycle.MutableLiveData
import com.tompee.utilities.knowyourmeds.base.BaseInteractor
import com.tompee.utilities.knowyourmeds.base.BaseViewModel
import com.tompee.utilities.knowyourmeds.extensions.default
import com.tompee.utilities.knowyourmeds.model.SchedulerPool

abstract class ListViewModel<I : BaseInteractor, T>(interactor: I, schedulerPool: SchedulerPool) :
        BaseViewModel<I>(interactor, schedulerPool) {
    val showSearchButton = MutableLiveData<Boolean>().default(true)
    val list = MutableLiveData<List<T>>()
    val count = MutableLiveData<Int>()
    val title = MutableLiveData<String>()
    val searching = MutableLiveData<Boolean>().default(false)
    val isListEmpty = MutableLiveData<Boolean>()

    open fun search() {
        showSearchButton.postValue(false)
    }
}