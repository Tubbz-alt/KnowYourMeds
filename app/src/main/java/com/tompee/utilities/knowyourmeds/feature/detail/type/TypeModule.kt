package com.tompee.utilities.knowyourmeds.feature.detail.type

import com.tompee.utilities.knowyourmeds.interactor.DetailInteractor
import com.tompee.utilities.knowyourmeds.model.SchedulerPool
import dagger.Module
import dagger.Provides

@Module
class TypeModule {
    @Provides
    fun provideTypePresenter(detailInteractor: DetailInteractor,
                             schedulerPool: SchedulerPool): TypePresenter =
            TypePresenter(detailInteractor, schedulerPool)
}