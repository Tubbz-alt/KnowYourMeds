package com.tompee.utilities.knowyourmeds.feature.search

import com.tompee.utilities.knowyourmeds.base.BasePresenter
import com.tompee.utilities.knowyourmeds.interactor.SearchInteractor
import com.tompee.utilities.knowyourmeds.model.SchedulerPool

class SearchPresenter(searchInteractor: SearchInteractor,
                      schedulerPool: SchedulerPool,
                      private val searchViewPagerAdapter: SearchViewPagerAdapter) :
        BasePresenter<SearchView, SearchInteractor>(searchInteractor, schedulerPool) {

    override fun onAttachView() {
        view.setAdapter(searchViewPagerAdapter)

        addSubscription(interactor.getIsDisplayDisclaimer()
                .subscribe { result ->
                    if (result) view.showDisclaimer(true)
                })

        addSubscription(interactor.getIsDueForRater()
                .subscribe { result ->
                    if (result) view.showAppRater()
                })
    }

    override fun onDetachView() {
    }
}