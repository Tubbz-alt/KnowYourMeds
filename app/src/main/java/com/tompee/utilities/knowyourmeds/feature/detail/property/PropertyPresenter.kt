package com.tompee.utilities.knowyourmeds.feature.detail.property

import android.content.Context
import com.tompee.utilities.knowyourmeds.base.BasePresenter
import com.tompee.utilities.knowyourmeds.interactor.DetailInteractor
import com.tompee.utilities.knowyourmeds.model.SchedulerPool

class PropertyPresenter(detailInteractor: DetailInteractor,
                        schedulerPool: SchedulerPool,
                        private val context: Context) :
        BasePresenter<PropertyView, DetailInteractor>(detailInteractor, schedulerPool) {

    override fun onAttachView() {
        interactor.getStockMedicine()
                .observeOn(scheduler.main)
                .doOnSuccess { view.setIsPrescribable(it.isPrescribable) }
                .doOnSuccess { view.setTtyText(it.tty?.name(context) ?: "") }
                .doOnSuccess { view.setIngredientAdapter(if (it.ingredients.isNotEmpty()) ListAdapter(it.ingredients) else null) }
                .doOnSuccess { view.setSplAdapter(if (it.ingredients.isNotEmpty()) ListAdapter(it.spls.values.toList()) else null) }
                .subscribeOn(scheduler.computation)
                .subscribe()
    }

    override fun onDetachView() {
    }
}