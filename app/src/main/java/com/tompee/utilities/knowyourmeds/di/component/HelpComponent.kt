package com.tompee.utilities.knowyourmeds.di.component

import com.tompee.utilities.knowyourmeds.di.scope.HelpScope
import com.tompee.utilities.knowyourmeds.feature.help.HelpActivity
import dagger.Component

@HelpScope
@Component(dependencies = [AppComponent::class])
interface HelpComponent {
    fun inject(helpActivity: HelpActivity)
}