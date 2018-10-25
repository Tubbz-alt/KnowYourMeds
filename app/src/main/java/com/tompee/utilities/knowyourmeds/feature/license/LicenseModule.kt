package com.tompee.utilities.knowyourmeds.feature.license

import android.content.Context
import com.tompee.utilities.knowyourmeds.core.asset.AssetManager
import com.tompee.utilities.knowyourmeds.interactor.HelpInteractor
import com.tompee.utilities.knowyourmeds.model.SchedulerPool
import dagger.Module
import dagger.Provides

@Module
class LicenseModule {
    @Provides
    fun provideHelpInteractor(assetManager: AssetManager): HelpInteractor =
            HelpInteractor(assetManager)

    @Provides
    fun provideLicenseViewModelFactory(helpInteractor: HelpInteractor,
                                       schedulerPool: SchedulerPool,
                                       context: Context): LicenseViewModel.Factory =
            LicenseViewModel.Factory(helpInteractor, schedulerPool, context)
}
