package com.tompee.utilities.knowyourmeds.di.component

import com.tompee.utilities.knowyourmeds.di.module.DetailModule
import com.tompee.utilities.knowyourmeds.di.module.DetailPresenterModule
import com.tompee.utilities.knowyourmeds.di.scope.DetailScope
import com.tompee.utilities.knowyourmeds.feature.detail.DetailActivity
import com.tompee.utilities.knowyourmeds.feature.detail.menu.MenuDialog
import com.tompee.utilities.knowyourmeds.feature.detail.property.PropertyFragment
import com.tompee.utilities.knowyourmeds.feature.detail.type.TypeFragment
import dagger.Component

@DetailScope
@Component(modules = [DetailModule::class, DetailPresenterModule::class],
        dependencies = [AppComponent::class])
interface DetailComponent {
    fun inject(detailActivity: DetailActivity)
    fun inject(propertyFragment: PropertyFragment)
    fun inject(menuDialog: MenuDialog)
    fun inject(typeFragment: TypeFragment)
}