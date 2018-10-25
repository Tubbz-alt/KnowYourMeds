package com.tompee.utilities.knowyourmeds.feature.detail.interaction

import android.content.Context
import com.tompee.utilities.knowyourmeds.interactor.DetailInteractor
import com.tompee.utilities.knowyourmeds.model.SchedulerPool
import dagger.Module
import dagger.Provides

@Module
class InteractionModule {
    @Provides
    fun provideInteractionViewModelFactory(detailInteractor: DetailInteractor,
                                           schedulerPool: SchedulerPool,
                                           context: Context): InteractionViewModel.Factory =
            InteractionViewModel.Factory(detailInteractor, schedulerPool, context)

    @Provides
    fun provideInteractionAdapter(context: Context): InteractionAdapter = InteractionAdapter(context)
}