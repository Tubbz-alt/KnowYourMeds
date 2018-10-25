package com.tompee.utilities.knowyourmeds.feature.detail

import android.content.Context
import com.tompee.utilities.knowyourmeds.R
import com.tompee.utilities.knowyourmeds.core.asset.AssetManager
import com.tompee.utilities.knowyourmeds.di.scope.DetailScope
import com.tompee.utilities.knowyourmeds.feature.detail.interaction.InteractionFragment
import com.tompee.utilities.knowyourmeds.feature.detail.interaction.InteractionModule
import com.tompee.utilities.knowyourmeds.feature.detail.market.MarketDrugFragment
import com.tompee.utilities.knowyourmeds.feature.detail.market.MarketDrugModule
import com.tompee.utilities.knowyourmeds.feature.detail.property.PropertyFragment
import com.tompee.utilities.knowyourmeds.feature.detail.property.PropertyModule
import com.tompee.utilities.knowyourmeds.feature.detail.type.TypeFragment
import com.tompee.utilities.knowyourmeds.feature.detail.type.TypeModule
import com.tompee.utilities.knowyourmeds.interactor.DetailInteractor
import com.tompee.utilities.knowyourmeds.model.Brand
import com.tompee.utilities.knowyourmeds.model.BrandedDoseFormGroup
import com.tompee.utilities.knowyourmeds.model.BrandedDrugComponent
import com.tompee.utilities.knowyourmeds.model.BrandedDrugPack
import com.tompee.utilities.knowyourmeds.model.ClinicalDoseFormGroup
import com.tompee.utilities.knowyourmeds.model.ClinicalDrugComponent
import com.tompee.utilities.knowyourmeds.model.ClinicalDrugPack
import com.tompee.utilities.knowyourmeds.model.MedicineContainer
import com.tompee.utilities.knowyourmeds.model.SchedulerPool
import com.tompee.utilities.knowyourmeds.repo.MedicineRepo
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module(includes = [DetailModule.Bindings::class])
class DetailModule {

    @Module
    interface Bindings {
        @ContributesAndroidInjector(modules = [PropertyModule::class])
        fun bindPropertyFragment(): PropertyFragment

        @ContributesAndroidInjector(modules = [MarketDrugModule::class])
        fun bindMarketDrugFragment(): MarketDrugFragment

        @ContributesAndroidInjector(modules = [TypeModule::class])
        fun bindTypeFragment(): TypeFragment

        @ContributesAndroidInjector(modules = [InteractionModule::class])
        fun bindInteractionFragment(): InteractionFragment
    }

    @DetailScope
    @Provides
    fun provideDetailViewModelFactory(detailInteractor: DetailInteractor,
                                      schedulerPool: SchedulerPool,
                                      assetManager: AssetManager,
                                      context: Context): DetailViewModel.Factory =
            DetailViewModel.Factory(detailInteractor, schedulerPool, assetManager, context)

    @DetailScope
    @Provides
    fun provideDetailInteractor(medicineContainer: MedicineContainer,
                                medicineRepo: MedicineRepo): DetailInteractor =
            DetailInteractor(medicineContainer, medicineRepo)

    @DetailScope
    @Provides
    fun providePageAdapter(detailActivity: DetailActivity,
                           context: Context): DetailPageAdapter {
        val list = mapOf(
                context.getString(R.string.label_market_drugs) to MarketDrugFragment(),
                context.getString(R.string.label_brand_name) to TypeFragment.newInstance(Brand),
                context.getString(R.string.label_sbdc) to TypeFragment.newInstance(BrandedDrugComponent),
                context.getString(R.string.label_sbd) to TypeFragment.newInstance(BrandedDrugPack),
                context.getString(R.string.label_sbdg) to TypeFragment.newInstance(BrandedDoseFormGroup),
                context.getString(R.string.label_scdc) to TypeFragment.newInstance(ClinicalDrugComponent),
                context.getString(R.string.label_scd) to TypeFragment.newInstance(ClinicalDrugPack),
                context.getString(R.string.label_scdg) to TypeFragment.newInstance(ClinicalDoseFormGroup),
                context.getString(R.string.label_interaction) to InteractionFragment()
        )
        return DetailPageAdapter(list, detailActivity.supportFragmentManager)
    }
}