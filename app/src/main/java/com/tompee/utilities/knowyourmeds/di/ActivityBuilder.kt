package com.tompee.utilities.knowyourmeds.di

import com.tompee.utilities.knowyourmeds.di.scope.DetailScope
import com.tompee.utilities.knowyourmeds.di.scope.HelpScope
import com.tompee.utilities.knowyourmeds.di.scope.MarketDrugScope
import com.tompee.utilities.knowyourmeds.di.scope.SearchScope
import com.tompee.utilities.knowyourmeds.feature.detail.DetailActivity
import com.tompee.utilities.knowyourmeds.feature.detail.DetailModule
import com.tompee.utilities.knowyourmeds.feature.help.HelpActivity
import com.tompee.utilities.knowyourmeds.feature.search.SearchActivity
import com.tompee.utilities.knowyourmeds.feature.search.SearchModule
import com.tompee.utilities.knowyourmeds.feature.spl.MarketDrugActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ActivityBuilder {

    @SearchScope
    @ContributesAndroidInjector(modules = [SearchModule::class])
    abstract fun bindSearchActivity(): SearchActivity

    @DetailScope
    @ContributesAndroidInjector(modules = [DetailModule::class])
    abstract fun bindDetailActivity(): DetailActivity

    @MarketDrugScope
    @ContributesAndroidInjector
    abstract fun bindMarketDrugActivity(): MarketDrugActivity

    @HelpScope
    @ContributesAndroidInjector
    abstract fun bindHelpActivity(): HelpActivity

}