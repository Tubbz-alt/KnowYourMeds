package com.tompee.utilities.knowyourmeds.feature.detail.property

import com.tompee.utilities.knowyourmeds.base.BasePresenter
import com.tompee.utilities.knowyourmeds.interactor.DetailInteractor
import com.tompee.utilities.knowyourmeds.model.MarketDrug
import com.tompee.utilities.knowyourmeds.model.SchedulerPool

class PropertyPresenter(detailInteractor: DetailInteractor,
                        schedulerPool: SchedulerPool) :
        BasePresenter<PropertyView, DetailInteractor>(detailInteractor, schedulerPool) {

    private val marketListener = object : MarketDrugAdapter.ItemClickListener {
        override fun onItemClick(marketDrug: MarketDrug) {
            view.moveToMarketDrugActivity(marketDrug)
        }
    }

    private val ingredientListener = object : ListAdapter.ItemClickListener {
        override fun onItemClick(item: String) {
        }
    }

    override fun onAttachView() {
        interactor.getStockMedicine()
                .observeOn(scheduler.main)
                .doOnSuccess { view.setIsPrescribable(it.isPrescribable) }
                .doOnSuccess { view.setType(it.tty!!) }
                .doOnSuccess { view.setIngredientAdapter(if (it.ingredients.isNotEmpty()) ListAdapter(it.ingredients, ingredientListener) else null) }
                .doOnSuccess { view.setSplAdapter(if (it.ingredients.isNotEmpty()) MarketDrugAdapter(it.marketDrugList, marketListener) else null) }
                .subscribeOn(scheduler.computation)
                .subscribe()
    }

    override fun onDetachView() {
    }
}