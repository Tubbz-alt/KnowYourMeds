package com.tompee.utilities.knowyourmeds.feature.search.search

import com.tompee.utilities.knowyourmeds.interactor.SearchInteractor
import com.tompee.utilities.knowyourmeds.model.SchedulerPool
import dagger.Module
import dagger.Provides

@Module
class SearchBarModule {

    @Provides
    fun provideSearchBarPresenter(searchInteractor: SearchInteractor,
                                  schedulerPool: SchedulerPool): SearchBarPresenter =
            SearchBarPresenter(searchInteractor, schedulerPool)
}