package com.tompee.utilities.knowyourmeds.feature.detail

import com.tompee.utilities.knowyourmeds.base.BasePresenter
import com.tompee.utilities.knowyourmeds.feature.detail.property.PropertyFragment
import com.tompee.utilities.knowyourmeds.feature.detail.type.TypeFragment
import com.tompee.utilities.knowyourmeds.interactor.DetailInteractor
import com.tompee.utilities.knowyourmeds.model.Brand
import com.tompee.utilities.knowyourmeds.model.BrandedDoseFormGroup
import com.tompee.utilities.knowyourmeds.model.BrandedDrugComponent
import com.tompee.utilities.knowyourmeds.model.BrandedDrugPack
import com.tompee.utilities.knowyourmeds.model.ClinicalDoseFormGroup
import com.tompee.utilities.knowyourmeds.model.ClinicalDrugComponent
import com.tompee.utilities.knowyourmeds.model.ClinicalDrugPack
import com.tompee.utilities.knowyourmeds.model.Property
import com.tompee.utilities.knowyourmeds.model.SchedulerPool
import io.reactivex.Single

class DetailPresenter(detailInteractor: DetailInteractor,
                      schedulerPool: SchedulerPool,
                      private val propertyFragment: PropertyFragment,
                      private val brandFragment: TypeFragment,
                      private val brandedDrugComponentFragment: TypeFragment,
                      private val brandedDrugPackFragment: TypeFragment,
                      private val brandedDoseFormGroupFragment: TypeFragment,
                      private val clinicalDrugComponentFragment: TypeFragment,
                      private val clinicalDrugPackFragment: TypeFragment,
                      private val clinicalDoseFormGroupFragment: TypeFragment) :
        BasePresenter<DetailView, DetailInteractor>(detailInteractor, schedulerPool) {

    override fun onAttachView() {
        setupTitle()
        setupQuery()
        setupFragmentSwitch()
    }

    override fun onDetachView() {
    }

    private fun setupTitle() {
        interactor.getStockMedicine()
                .doOnSuccess { view.setTitle(it.name) }
                .subscribe()
    }

    private fun setupQuery() {
        addSubscription(
                Single.just("")
                        .doOnSuccess { view.showProcessingDialog() }
                        .flatMap {
                            interactor.getMedicine()
                                    .subscribeOn(scheduler.io)
                        }
                        .observeOn(scheduler.main)
                        .doOnSuccess { view.hideProcessingDialog() }
                        .doOnSuccess { view.setActiveFragment(propertyFragment) }
                        .subscribeOn(scheduler.main)
                        .subscribe())
    }

    private fun setupFragmentSwitch() {
        addSubscription(view.fragmentType()
                .doOnNext {
                    view.setActiveFragment(when (it) {
                        Property -> propertyFragment
                        Brand -> brandFragment
                        BrandedDrugComponent -> brandedDrugComponentFragment
                        BrandedDrugPack -> brandedDrugPackFragment
                        BrandedDoseFormGroup -> brandedDoseFormGroupFragment
                        ClinicalDrugComponent -> clinicalDrugComponentFragment
                        ClinicalDrugPack -> clinicalDrugPackFragment
                        ClinicalDoseFormGroup -> clinicalDoseFormGroupFragment
                        else -> propertyFragment
                    })
                }
                .subscribe())
    }
}