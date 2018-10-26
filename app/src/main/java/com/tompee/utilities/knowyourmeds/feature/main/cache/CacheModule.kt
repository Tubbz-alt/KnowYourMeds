package com.tompee.utilities.knowyourmeds.feature.main.cache

import android.content.Context
import com.tompee.utilities.knowyourmeds.interactor.SearchInteractor
import com.tompee.utilities.knowyourmeds.model.SchedulerPool
import dagger.Module
import dagger.Provides

@Module
class CacheModule {
    @Provides
    fun provideCacheViewModelFactory(searchInteractor: SearchInteractor,
                                     schedulerPool: SchedulerPool,
                                     context: Context): CacheViewModel.Factory =
            CacheViewModel.Factory(searchInteractor, schedulerPool, context)

    @Provides
    fun provideCacheListAdapter() : CacheListAdapter = CacheListAdapter()
}