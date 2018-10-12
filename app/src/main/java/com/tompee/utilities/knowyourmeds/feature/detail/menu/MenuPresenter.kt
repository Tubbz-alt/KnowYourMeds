package com.tompee.utilities.knowyourmeds.feature.detail.menu

import com.tompee.utilities.knowyourmeds.R
import com.tompee.utilities.knowyourmeds.base.BasePresenter
import com.tompee.utilities.knowyourmeds.interactor.DetailInteractor
import com.tompee.utilities.knowyourmeds.model.Brand
import com.tompee.utilities.knowyourmeds.model.BrandedDoseFormGroup
import com.tompee.utilities.knowyourmeds.model.BrandedDrugComponent
import com.tompee.utilities.knowyourmeds.model.BrandedDrugPack
import com.tompee.utilities.knowyourmeds.model.ClinicalDoseFormGroup
import com.tompee.utilities.knowyourmeds.model.ClinicalDrugComponent
import com.tompee.utilities.knowyourmeds.model.ClinicalDrugPack
import com.tompee.utilities.knowyourmeds.model.Interaction
import com.tompee.utilities.knowyourmeds.model.Page
import com.tompee.utilities.knowyourmeds.model.Property
import com.tompee.utilities.knowyourmeds.model.SchedulerPool
import com.tompee.utilities.knowyourmeds.model.Sources

class MenuPresenter(detailInteractor: DetailInteractor,
                    schedulerPool: SchedulerPool) :
        BasePresenter<MenuView, DetailInteractor>(detailInteractor, schedulerPool) {
    override fun onAttachView() {
        addSubscription(interactor.getStockMedicine()
                .doOnSuccess { view.createMenuView(R.string.tab_properties, R.color.properties, Property) }
                .doOnSuccess { if (it.brands.isNotEmpty()) view.createMenuView(R.string.tab_brands, R.color.brand, Brand) }
                .doOnSuccess { if (it.sbdc.isNotEmpty()) view.createMenuView(R.string.tab_sbdc, R.color.sbdc, BrandedDrugComponent) }
                .doOnSuccess { if (it.sbd.isNotEmpty()) view.createMenuView(R.string.tab_sbd, R.color.sbd, BrandedDrugPack) }
                .doOnSuccess { if (it.sbdg.isNotEmpty()) view.createMenuView(R.string.tab_sbdg, R.color.sbdg, BrandedDoseFormGroup) }
                .doOnSuccess { if (it.scdc.isNotEmpty()) view.createMenuView(R.string.tab_scdc, R.color.scdc, ClinicalDrugComponent) }
                .doOnSuccess { if (it.scd.isNotEmpty()) view.createMenuView(R.string.tab_scd, R.color.scd, ClinicalDrugPack) }
                .doOnSuccess { if (it.scdg.isNotEmpty()) view.createMenuView(R.string.tab_scdg, R.color.scdg, ClinicalDoseFormGroup) }
                .doOnSuccess { if (it.url.isNotEmpty()) view.createMenuView(R.string.tab_info, R.color.info, Page) }
                .doOnSuccess { if (it.interactions.isNotEmpty()) view.createMenuView(R.string.tab_interaction, R.color.interaction, Interaction) }
                .doOnSuccess { if (it.sources.isNotEmpty()) view.createMenuView(R.string.tab_sources, R.color.sources, Sources) }
                .subscribe())
    }

    override fun onDetachView() {
    }
}