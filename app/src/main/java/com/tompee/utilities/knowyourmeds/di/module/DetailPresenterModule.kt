package com.tompee.utilities.knowyourmeds.di.module

import android.content.Context
import com.tompee.utilities.knowyourmeds.di.scope.DetailScope
import com.tompee.utilities.knowyourmeds.feature.detail.DetailPresenter
import com.tompee.utilities.knowyourmeds.feature.detail.menu.MenuPresenter
import com.tompee.utilities.knowyourmeds.feature.detail.property.PropertyFragment
import com.tompee.utilities.knowyourmeds.feature.detail.property.PropertyPresenter
import com.tompee.utilities.knowyourmeds.feature.detail.type.TypeFragment
import com.tompee.utilities.knowyourmeds.feature.detail.type.TypePresenter
import com.tompee.utilities.knowyourmeds.interactor.DetailInteractor
import com.tompee.utilities.knowyourmeds.model.SchedulerPool
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class DetailPresenterModule {
    @DetailScope
    @Provides
    fun provideDetailPresenter(detailInteractor: DetailInteractor,
                               schedulerPool: SchedulerPool,
                               propertyFragment: PropertyFragment,
                               @Named("brand") brandFragment: TypeFragment,
                               @Named("sbdc") brandedDrugComponentFragment: TypeFragment,
                               @Named("sbd") brandedDrugPackFragment: TypeFragment,
                               @Named("sbdg") brandedDoseFormGroupFragment: TypeFragment,
                               @Named("scdc") clinicalDrugComponentFragment: TypeFragment,
                               @Named("scd") clinicalDrugPackFragment: TypeFragment,
                               @Named("scdg") clinicalDoseFormGroupFragment : TypeFragment): DetailPresenter =
            DetailPresenter(detailInteractor, schedulerPool,
                    propertyFragment,
                    brandFragment,
                    brandedDrugComponentFragment,
                    brandedDrugPackFragment,
                    brandedDoseFormGroupFragment,
                    clinicalDrugComponentFragment,
                    clinicalDrugPackFragment,
                    clinicalDoseFormGroupFragment)

    @DetailScope
    @Provides
    fun providePropertyPresenter(detailInteractor: DetailInteractor,
                                 schedulerPool: SchedulerPool,
                                 context: Context): PropertyPresenter =
            PropertyPresenter(detailInteractor, schedulerPool, context)

    @DetailScope
    @Provides
    fun provideMenuPresenter(detailInteractor: DetailInteractor,
                             schedulerPool: SchedulerPool): MenuPresenter =
            MenuPresenter(detailInteractor, schedulerPool)

    @DetailScope
    @Provides
    fun provideTypePresenter(detailInteractor: DetailInteractor,
                             schedulerPool: SchedulerPool): TypePresenter =
            TypePresenter(detailInteractor, schedulerPool)
}