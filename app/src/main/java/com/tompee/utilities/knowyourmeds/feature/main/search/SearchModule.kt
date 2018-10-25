package com.tompee.utilities.knowyourmeds.feature.main.search

import com.tompee.utilities.knowyourmeds.feature.common.ListTextAdapter
import com.tompee.utilities.knowyourmeds.interactor.SearchInteractor
import com.tompee.utilities.knowyourmeds.model.SchedulerPool
import dagger.Module
import dagger.Provides

@Module
class SearchModule {

    @Provides
    fun provideSearchViewModelFactory(searchInteractor: SearchInteractor,
                                      schedulerPool: SchedulerPool): SearchViewModel.Factory =
            SearchViewModel.Factory(searchInteractor, schedulerPool)

    @Provides
    fun provideListAdapter() : ListTextAdapter = ListTextAdapter()
}