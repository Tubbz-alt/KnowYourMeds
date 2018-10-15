package com.tompee.utilities.knowyourmeds.di.module

import android.content.Context
import com.tompee.utilities.knowyourmeds.di.scope.DetailScope
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
import com.tompee.utilities.knowyourmeds.model.MedicineContainer
import com.tompee.utilities.knowyourmeds.repo.MedicineRepo
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class DetailModule {
    @DetailScope
    @Provides
    fun provideDetailInteractor(medicineRepo: MedicineRepo,
                                medicineContainer: MedicineContainer): DetailInteractor =
            DetailInteractor(medicineRepo, medicineContainer)

    @DetailScope
    @Provides
    fun providePropertyFragment(): PropertyFragment = PropertyFragment.getInstance()

    @DetailScope
    @Provides
    @Named("brand")
    fun provideBrandFragment(context: Context): TypeFragment =
            TypeFragment.newInstance(Brand.name(context), Brand.tag)

    @DetailScope
    @Provides
    @Named("sbdc")
    fun provideBrandedDrugComponentFragment(context: Context): TypeFragment =
            TypeFragment.newInstance(BrandedDrugComponent.name(context), BrandedDrugComponent.tag)

    @DetailScope
    @Provides
    @Named("sbd")
    fun provideBrandedDrugPackFragment(context: Context): TypeFragment =
            TypeFragment.newInstance(BrandedDrugPack.name(context), BrandedDrugPack.tag)

    @DetailScope
    @Provides
    @Named("sbdg")
    fun provideBrandedDoseFormGroupFragment(context: Context): TypeFragment =
            TypeFragment.newInstance(BrandedDoseFormGroup.name(context), BrandedDoseFormGroup.tag)

    @DetailScope
    @Provides
    @Named("scdc")
    fun provideClinicalDrugComponentFragment(context: Context): TypeFragment =
            TypeFragment.newInstance(ClinicalDrugComponent.name(context), ClinicalDrugComponent.tag)

    @DetailScope
    @Provides
    @Named("scd")
    fun provideClinicalDrugPackFragment(context: Context): TypeFragment =
            TypeFragment.newInstance(ClinicalDrugPack.name(context), ClinicalDrugPack.tag)

    @DetailScope
    @Provides
    @Named("scdg")
    fun provideClinicalDoseFormGroupFragment(context: Context): TypeFragment =
            TypeFragment.newInstance(ClinicalDoseFormGroup.name(context), ClinicalDoseFormGroup.tag)

}