package com.tompee.utilities.knowyourmeds.feature.detail.type

import com.tompee.utilities.knowyourmeds.base.BasePresenter
import com.tompee.utilities.knowyourmeds.interactor.DetailInteractor
import com.tompee.utilities.knowyourmeds.model.Brand
import com.tompee.utilities.knowyourmeds.model.BrandedDoseFormGroup
import com.tompee.utilities.knowyourmeds.model.BrandedDrugComponent
import com.tompee.utilities.knowyourmeds.model.BrandedDrugPack
import com.tompee.utilities.knowyourmeds.model.ClinicalDoseFormGroup
import com.tompee.utilities.knowyourmeds.model.ClinicalDrugComponent
import com.tompee.utilities.knowyourmeds.model.ClinicalDrugPack
import com.tompee.utilities.knowyourmeds.model.SchedulerPool

class TypePresenter(detailInteractor: DetailInteractor,
                    schedulerPool: SchedulerPool) :
        BasePresenter<TypeView, DetailInteractor>(detailInteractor, schedulerPool) {

    override fun onAttachView() {
        interactor.getStockMedicine()
                .doOnSuccess {
                    val list = when (view.getType()) {
                        Brand -> it.brands
                        BrandedDrugComponent -> it.sbdc
                        BrandedDrugPack -> it.sbd
                        BrandedDoseFormGroup -> it.sbdg
                        ClinicalDrugComponent -> it.scdc
                        ClinicalDrugPack -> it.scd
                        ClinicalDoseFormGroup -> it.scdg
                        else -> it.scdg
                    }
                    view.setAdapter(ListAdapter(list))
                }.subscribe()
    }

    override fun onDetachView() {
    }
}