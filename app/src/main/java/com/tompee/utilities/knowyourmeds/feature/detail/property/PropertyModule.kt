package com.tompee.utilities.knowyourmeds.feature.detail.property

import com.tompee.utilities.knowyourmeds.feature.common.ListTextAdapter
import com.tompee.utilities.knowyourmeds.interactor.DetailInteractor
import com.tompee.utilities.knowyourmeds.model.SchedulerPool
import dagger.Module
import dagger.Provides

@Module
class PropertyModule {

    @Provides
    fun providePropertyViewModelFactory(detailInteractor: DetailInteractor,
                                        schedulerPool: SchedulerPool): PropertyViewModel.Factory =
            PropertyViewModel.Factory(detailInteractor, schedulerPool)

    @Provides
    fun provideListAdapter() : ListTextAdapter = ListTextAdapter()
}