package com.tompee.utilities.knowyourmeds.feature.about

import android.content.Context
import com.tompee.utilities.knowyourmeds.core.asset.AssetManager
import com.tompee.utilities.knowyourmeds.interactor.HelpInteractor
import com.tompee.utilities.knowyourmeds.model.SchedulerPool
import dagger.Module
import dagger.Provides

@Module
class AboutModule {
    @Provides
    fun provideHelpInteractor(assetManager: AssetManager): HelpInteractor =
            HelpInteractor(assetManager)

    @Provides
    fun provideAboutViewModelFactory(helpInteractor: HelpInteractor,
                                     schedulerPool: SchedulerPool,
                                     context: Context): AboutViewModel.Factory =
            AboutViewModel.Factory(helpInteractor, schedulerPool, context)
}
