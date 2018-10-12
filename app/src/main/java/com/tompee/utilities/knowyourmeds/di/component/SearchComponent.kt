package com.tompee.utilities.knowyourmeds.di.component

import com.tompee.utilities.knowyourmeds.di.module.SearchModule
import com.tompee.utilities.knowyourmeds.di.scope.SearchScope
import com.tompee.utilities.knowyourmeds.feature.search.SearchActivity
import com.tompee.utilities.knowyourmeds.feature.search.search.SearchBarFragment
import dagger.Component

@SearchScope
@Component(modules = [SearchModule::class],
        dependencies = [AppComponent::class])
interface SearchComponent {
    fun inject(searchActivity: SearchActivity)
    fun inject(searchBarFragment: SearchBarFragment)
}