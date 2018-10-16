package com.tompee.utilities.knowyourmeds.feature.detail.page

import com.tompee.utilities.knowyourmeds.interactor.DetailInteractor
import com.tompee.utilities.knowyourmeds.model.SchedulerPool
import dagger.Module
import dagger.Provides

@Module
class PageModule {
    @Provides
    fun providePagePresenter(detailInteractor: DetailInteractor,
                             schedulerPool: SchedulerPool): PagePresenter =
            PagePresenter(detailInteractor, schedulerPool)

}