package com.tompee.utilities.knowyourmeds.feature.main

import android.content.Context
import com.tompee.utilities.knowyourmeds.core.api.MedApi
import com.tompee.utilities.knowyourmeds.core.asset.AssetManager
import com.tompee.utilities.knowyourmeds.core.preferences.Preferences
import com.tompee.utilities.knowyourmeds.di.scope.MainScope
import com.tompee.utilities.knowyourmeds.feature.main.disclaimer.DisclaimerDialog
import com.tompee.utilities.knowyourmeds.feature.main.rater.AppRaterDialog
import com.tompee.utilities.knowyourmeds.feature.main.search.SearchFragment
import com.tompee.utilities.knowyourmeds.feature.main.search.SearchModule
import com.tompee.utilities.knowyourmeds.interactor.SearchInteractor
import com.tompee.utilities.knowyourmeds.model.MedicineContainer
import com.tompee.utilities.knowyourmeds.model.SchedulerPool
import com.tompee.utilities.knowyourmeds.repo.MedicineRepo
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module(includes = [MainModule.Bindings::class])
class MainModule {

    @Module
    interface Bindings {
        @ContributesAndroidInjector(modules = [SearchModule::class])
        fun bindSearchFragment(): SearchFragment

        @ContributesAndroidInjector
        fun bindDisclaimerDialog(): DisclaimerDialog

        @ContributesAndroidInjector
        fun bindAppRaterDialog(): AppRaterDialog
    }

    @MainScope
    @Provides
    fun provideMainViewModelFactory(searchInteractor: SearchInteractor,
                                    schedulerPool: SchedulerPool,
                                    assetManager: AssetManager,
                                    context: Context): MainViewModel.Factory =
            MainViewModel.Factory(searchInteractor, schedulerPool, assetManager, context)

    @MainScope
    @Provides
    fun provideSearchInteractor(medApi: MedApi,
                                medicineRepo: MedicineRepo,
                                medicineContainer: MedicineContainer,
                                preferences: Preferences): SearchInteractor =
            SearchInteractor(medApi, medicineRepo, medicineContainer, preferences)
}