package com.tompee.utilities.knowyourmeds.feature.detail.page

import com.tompee.utilities.knowyourmeds.base.BasePresenter
import com.tompee.utilities.knowyourmeds.interactor.DetailInteractor
import com.tompee.utilities.knowyourmeds.model.SchedulerPool

class PagePresenter(detailInteractor: DetailInteractor,
                    schedulerPool: SchedulerPool) :
        BasePresenter<PageView, DetailInteractor>(detailInteractor, schedulerPool) {
    override fun onAttachView() {
        addSubscription(interactor.getStockMedicine()
                .subscribe { it -> view.loadUrl(it.url) })
    }

    override fun onDetachView() {
    }
}