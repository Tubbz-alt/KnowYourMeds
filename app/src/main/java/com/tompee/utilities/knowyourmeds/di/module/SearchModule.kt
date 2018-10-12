package com.tompee.utilities.knowyourmeds.di.module

import android.content.Context
import android.support.v4.app.FragmentManager
import com.tompee.utilities.knowyourmeds.core.api.MedApi
import com.tompee.utilities.knowyourmeds.core.preferences.Preferences
import com.tompee.utilities.knowyourmeds.di.scope.SearchScope
import com.tompee.utilities.knowyourmeds.feature.search.SearchPresenter
import com.tompee.utilities.knowyourmeds.feature.search.SearchViewPagerAdapter
import com.tompee.utilities.knowyourmeds.feature.search.persist.RecentFavoriteFragment
import com.tompee.utilities.knowyourmeds.feature.search.search.SearchBarPresenter
import com.tompee.utilities.knowyourmeds.interactor.SearchInteractor
import com.tompee.utilities.knowyourmeds.model.MedicineContainer
import com.tompee.utilities.knowyourmeds.model.SchedulerPool
import com.tompee.utilities.knowyourmeds.repo.MedicineRepo
import dagger.Module
import dagger.Provides

@Module
class SearchModule(private val fragmentManager: FragmentManager) {
    @Provides
    @SearchScope
    fun provideSearchPagerAdapter(context: Context): SearchViewPagerAdapter =
            SearchViewPagerAdapter(context, RecentFavoriteFragment.newInstance(true),
                    RecentFavoriteFragment.newInstance(false), fragmentManager)

    @Provides
    @SearchScope
    fun provideSearchPresenter(searchInteractor: SearchInteractor,
                               schedulerPool: SchedulerPool,
                               searchViewPagerAdapter: SearchViewPagerAdapter): SearchPresenter =
            SearchPresenter(searchInteractor, schedulerPool, searchViewPagerAdapter)

    @Provides
    @SearchScope
    fun provideSearchInteractor(preferences: Preferences,
                                medApi: MedApi,
                                medicineRepo: MedicineRepo,
                                medicineContainer: MedicineContainer):
            SearchInteractor = SearchInteractor(preferences, medApi, medicineRepo, medicineContainer)

    @Provides
    @SearchScope
    fun provideSearchBarPresenter(searchInteractor: SearchInteractor,
                                  schedulerPool: SchedulerPool): SearchBarPresenter =
            SearchBarPresenter(searchInteractor, schedulerPool)
}