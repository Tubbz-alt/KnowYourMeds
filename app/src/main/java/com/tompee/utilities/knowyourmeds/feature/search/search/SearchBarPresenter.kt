package com.tompee.utilities.knowyourmeds.feature.search.search

import com.tompee.utilities.knowyourmeds.base.BasePresenter
import com.tompee.utilities.knowyourmeds.interactor.SearchInteractor
import com.tompee.utilities.knowyourmeds.model.SchedulerPool

class SearchBarPresenter(searchInteractor: SearchInteractor,
                         schedulerPool: SchedulerPool) :
        BasePresenter<SearchBarView, SearchInteractor>(searchInteractor, schedulerPool) {

    override fun onAttachView() {
        setupSearch()
        setupDetailedSearch()
    }

    override fun onDetachView() {
    }

    private fun setupSearch() {
        addSubscription(view.searchString()
                .map { it.isEmpty() }
                .filter { it }
                .subscribe { view.showInvalidSearchString() })

        addSubscription(view.searchString()
                .filter { it.isNotEmpty() }
                .doOnNext { view.showStartSearchSequence() }
                .flatMapSingle {
                    interactor.getMedicine(it)
                            .subscribeOn(scheduler.io)
                }
                .observeOn(scheduler.main)
                .doOnNext { view.showEndSearchSequence() }
                .subscribe(view::showSearchResults))
    }

    private fun setupDetailedSearch() {
        addSubscription(view.detailedSearchRequest()
                .subscribe { view.moveToDetailActivity() })
    }
}