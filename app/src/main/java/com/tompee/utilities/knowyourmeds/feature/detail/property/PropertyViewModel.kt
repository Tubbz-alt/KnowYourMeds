package com.tompee.utilities.knowyourmeds.feature.detail.property

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tompee.utilities.knowyourmeds.base.BaseViewModel
import com.tompee.utilities.knowyourmeds.extensions.default
import com.tompee.utilities.knowyourmeds.interactor.DetailInteractor
import com.tompee.utilities.knowyourmeds.model.Medicine
import com.tompee.utilities.knowyourmeds.model.SchedulerPool
import io.reactivex.rxkotlin.plusAssign

class PropertyViewModel private constructor(detailInteractor: DetailInteractor,
                                            schedulerPool: SchedulerPool) :
        BaseViewModel<DetailInteractor>(detailInteractor,
                schedulerPool) {

    class Factory(private val detailInteractor: DetailInteractor,
                  private val schedulerPool: SchedulerPool) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return PropertyViewModel(detailInteractor, schedulerPool) as T
        }
    }

    val medicine = MutableLiveData<Medicine>()
    val showIngredients = MutableLiveData<Boolean>().default(false)
    val withUrl = MutableLiveData<Boolean>()
    val url = MutableLiveData<String>()

    init {
        subscriptions += interactor.getStockMedicine()
                .doOnSuccess {
                    showIngredients.postValue(it.ingredientList.isNotEmpty())
                    withUrl.postValue(it.url.isNotEmpty())
                    url.postValue(it.url)
                }
                .subscribe(medicine::postValue)
    }
}