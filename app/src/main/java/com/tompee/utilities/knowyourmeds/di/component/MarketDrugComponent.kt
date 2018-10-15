package com.tompee.utilities.knowyourmeds.di.component

import com.tompee.utilities.knowyourmeds.di.scope.MarketDrugScope
import com.tompee.utilities.knowyourmeds.feature.spl.MarketDrugActivity
import dagger.Component

@MarketDrugScope
@Component(dependencies = [AppComponent::class])
interface MarketDrugComponent {
    fun inject(marketDrugActivity: MarketDrugActivity)
}