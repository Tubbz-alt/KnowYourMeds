package com.tompee.utilities.knowyourmeds.feature.detail.market

import android.content.Context
import com.tompee.utilities.knowyourmeds.interactor.DetailInteractor
import com.tompee.utilities.knowyourmeds.model.SchedulerPool
import dagger.Module
import dagger.Provides

@Module
class MarketDrugModule {
    @Provides
    fun provideMarketDrugViewModelFactory(detailInteractor: DetailInteractor,
                                          schedulerPool: SchedulerPool,
                                          context: Context): MarketDrugViewModel.Factory =
            MarketDrugViewModel.Factory(detailInteractor, schedulerPool, context)

    @Provides
    fun provideMarketDrugAdapter(): MarketDrugAdapter = MarketDrugAdapter()
}