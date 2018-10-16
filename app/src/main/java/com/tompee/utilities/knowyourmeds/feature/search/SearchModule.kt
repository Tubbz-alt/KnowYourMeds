package com.tompee.utilities.knowyourmeds.feature.search

import android.content.Context
import android.support.v4.app.FragmentManager
import com.tompee.utilities.knowyourmeds.core.api.MedApi
import com.tompee.utilities.knowyourmeds.core.preferences.Preferences
import com.tompee.utilities.knowyourmeds.di.scope.SearchScope
import com.tompee.utilities.knowyourmeds.feature.search.persist.RecentFavoriteFragment
import com.tompee.utilities.knowyourmeds.feature.search.search.SearchBarFragment
import com.tompee.utilities.knowyourmeds.feature.search.search.SearchBarModule
import com.tompee.utilities.knowyourmeds.interactor.SearchInteractor
import com.tompee.utilities.knowyourmeds.model.MedicineContainer
import com.tompee.utilities.knowyourmeds.model.SchedulerPool
import com.tompee.utilities.knowyourmeds.repo.MedicineRepo
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module(includes = [SearchModule.Bindings::class])
class SearchModule {
    @Module
    interface Bindings {
        @ContributesAndroidInjector(modules = [SearchBarModule::class])
        fun bindSearchBarFragment(): SearchBarFragment
    }

    @SearchScope
    @Provides
    fun provideFragmentManager(searchActivity: SearchActivity): FragmentManager = searchActivity.supportFragmentManager

    @SearchScope
    @Provides
    fun provideSearchInteractor(preferences: Preferences,
                                medApi: MedApi,
                                medicineRepo: MedicineRepo,
                                medicineContainer: MedicineContainer):
            SearchInteractor = SearchInteractor(preferences, medApi, medicineRepo, medicineContainer)

    @SearchScope
    @Provides
    fun provideSearchPresenter(searchInteractor: SearchInteractor,
                               schedulerPool: SchedulerPool,
                               searchViewPagerAdapter: SearchViewPagerAdapter): SearchPresenter =
            SearchPresenter(searchInteractor, schedulerPool, searchViewPagerAdapter)

    @Provides
    @SearchScope
    fun provideSearchPagerAdapter(context: Context,
                                  fragmentManager: FragmentManager): SearchViewPagerAdapter =
            SearchViewPagerAdapter(context, RecentFavoriteFragment.newInstance(true),
                    RecentFavoriteFragment.newInstance(false), fragmentManager)

}