package com.tompee.utilities.knowyourmeds.feature.detail.type

import android.content.Context
import com.tompee.utilities.knowyourmeds.interactor.DetailInteractor
import com.tompee.utilities.knowyourmeds.model.SchedulerPool
import dagger.Module
import dagger.Provides

@Module
class TypeModule {
    @Provides
    fun provideTypeViewModelFactory(detailInteractor: DetailInteractor,
                                    schedulerPool: SchedulerPool,
                                    context: Context): TypeViewModel.Factory =
            TypeViewModel.Factory(detailInteractor, schedulerPool, context)

    @Provides
    fun provideTypeAdapter(context: Context): TypeAdapter = TypeAdapter(context)
}