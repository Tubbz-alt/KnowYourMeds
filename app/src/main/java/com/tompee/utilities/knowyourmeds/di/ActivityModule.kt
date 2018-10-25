package com.tompee.utilities.knowyourmeds.di

import com.tompee.utilities.knowyourmeds.di.scope.DetailScope
import com.tompee.utilities.knowyourmeds.di.scope.MainScope
import com.tompee.utilities.knowyourmeds.feature.about.AboutActivity
import com.tompee.utilities.knowyourmeds.feature.about.AboutModule
import com.tompee.utilities.knowyourmeds.feature.detail.DetailActivity
import com.tompee.utilities.knowyourmeds.feature.detail.DetailModule
import com.tompee.utilities.knowyourmeds.feature.license.LicenseActivity
import com.tompee.utilities.knowyourmeds.feature.license.LicenseModule
import com.tompee.utilities.knowyourmeds.feature.main.MainActivity
import com.tompee.utilities.knowyourmeds.feature.main.MainModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @MainScope
    @ContributesAndroidInjector(modules = [MainModule::class])
    abstract fun bindMainActivity(): MainActivity

    @DetailScope
    @ContributesAndroidInjector(modules = [DetailModule::class])
    abstract fun bindDetailActivity(): DetailActivity

    @ContributesAndroidInjector(modules = [LicenseModule::class])
    abstract fun bindLicenseActivity(): LicenseActivity

    @ContributesAndroidInjector(modules = [AboutModule::class])
    abstract fun bindAboutActivity(): AboutActivity
}
