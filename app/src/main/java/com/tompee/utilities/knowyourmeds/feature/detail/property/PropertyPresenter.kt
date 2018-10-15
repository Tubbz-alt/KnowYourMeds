package com.tompee.utilities.knowyourmeds.feature.detail.property

import com.tompee.utilities.knowyourmeds.base.BasePresenter
import com.tompee.utilities.knowyourmeds.interactor.DetailInteractor
import com.tompee.utilities.knowyourmeds.model.MarketDrug
import com.tompee.utilities.knowyourmeds.model.SchedulerPool

class PropertyPresenter(detailInteractor: DetailInteractor,
                        schedulerPool: SchedulerPool) :
        BasePresenter<PropertyView, DetailInteractor>(detailInteractor, schedulerPool) {

    private val listener = object : MarketDrugAdapter.ItemClickListener {
        override fun onItemClick(marketDrug: MarketDrug) {
            view.moveToMarketDrugActivity(marketDrug)
        }
    }

    override fun onAttachView() {
        interactor.getStockMedicine()
                .observeOn(scheduler.main)
                .doOnSuccess { view.setIsPrescribable(it.isPrescribable) }
                .doOnSuccess { view.setType(it.tty!!) }
                .doOnSuccess { view.setIngredientAdapter(if (it.ingredients.isNotEmpty()) ListAdapter(it.ingredients) else null) }
                .doOnSuccess { view.setSplAdapter(if (it.ingredients.isNotEmpty()) MarketDrugAdapter(it.marketDrugList, listener) else null) }
                .subscribeOn(scheduler.computation)
                .subscribe()
    }

    override fun onDetachView() {
    }
}