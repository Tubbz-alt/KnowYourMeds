package com.tompee.utilities.knowyourmeds.feature.detail.property

import com.tompee.utilities.knowyourmeds.interactor.DetailInteractor
import com.tompee.utilities.knowyourmeds.model.SchedulerPool
import dagger.Module
import dagger.Provides

@Module
class PropertyModule {
    @Provides
    fun providePropertyPresenter(detailInteractor: DetailInteractor,
                                 schedulerPool: SchedulerPool): PropertyPresenter =
            PropertyPresenter(detailInteractor, schedulerPool)
}