package com.tompee.utilities.knowyourmeds.feature.main.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tompee.utilities.knowyourmeds.base.BaseViewModel
import com.tompee.utilities.knowyourmeds.extensions.default
import com.tompee.utilities.knowyourmeds.interactor.SearchInteractor
import com.tompee.utilities.knowyourmeds.model.Medicine
import com.tompee.utilities.knowyourmeds.model.SchedulerPool
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.plusAssign

class SearchViewModel private constructor(searchInteractor: SearchInteractor,
                                          schedulerPool: SchedulerPool) :
        BaseViewModel<SearchInteractor>(searchInteractor, schedulerPool) {

    class Factory(private val searchInteractor: SearchInteractor,
                  private val schedulerPool: SchedulerPool) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return SearchViewModel(searchInteractor, schedulerPool) as T
        }
    }

    val searching = MutableLiveData<Boolean>().default(false)
    val suggestionList = MutableLiveData<List<String>>()
    val showResult = MutableLiveData<Boolean>().default(false)
    val showSuggestion = MutableLiveData<Boolean>().default(false)
    val result = MutableLiveData<Medicine>()
    val query = MutableLiveData<String>()
    val showError = MutableLiveData<Boolean>().default(false)

    fun searchMedicine(name: String) {
        val search = Completable.fromAction {
            searching.postValue(true)
            showResult.postValue(false)
            showSuggestion.postValue(false)
        }
                .andThen(Single.just(name))
                .filter { it.isNotEmpty() }
                .doOnSuccess(query::postValue)
                .flatMapSingle(interactor::searchMedicine)
                .doOnSuccess { searching.postValue(false) }
                .cache()

        subscriptions += search.filter { it.size == 1 }
                .map { it.first() }
                .doOnSuccess { showResult.postValue(true) }
                .subscribeOn(schedulerPool.io)
                .subscribe(result::postValue) { }

        subscriptions += search.filter { it.size > 1 }
                .flatMapSingle { list ->
                    Observable.fromIterable(list)
                            .map { it.name }
                            .toList()
                }
                .doOnSuccess { showSuggestion.postValue(true) }
                .subscribeOn(schedulerPool.io)
                .subscribe(suggestionList::postValue) { }

        subscriptions += search.map { it.isEmpty() }
                .subscribeOn(schedulerPool.io)
                .subscribe(showError::postValue) { }
    }
}