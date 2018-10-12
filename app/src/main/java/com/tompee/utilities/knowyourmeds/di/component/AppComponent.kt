package com.tompee.utilities.knowyourmeds.di.component

import android.content.Context
import com.tompee.utilities.knowyourmeds.KnowYourMedsApplication
import com.tompee.utilities.knowyourmeds.core.api.DailyMedApi
import com.tompee.utilities.knowyourmeds.core.api.MedApi
import com.tompee.utilities.knowyourmeds.core.api.MedlineApi
import com.tompee.utilities.knowyourmeds.core.asset.AssetManager
import com.tompee.utilities.knowyourmeds.core.preferences.Preferences
import com.tompee.utilities.knowyourmeds.di.module.AppModule
import com.tompee.utilities.knowyourmeds.di.module.CoreModule
import com.tompee.utilities.knowyourmeds.model.MedicineContainer
import com.tompee.utilities.knowyourmeds.model.SchedulerPool
import com.tompee.utilities.knowyourmeds.repo.MedicineRepo
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, CoreModule::class])
interface AppComponent {
    fun application(): KnowYourMedsApplication
    fun context(): Context

    fun assetManager(): AssetManager
    fun preferences(): Preferences
    fun medApi(): MedApi
    fun medlineApi(): MedlineApi
    fun dailyMedApi(): DailyMedApi
    fun medicineContainer(): MedicineContainer

    fun medicineRepo(): MedicineRepo

    fun schedulerPool(): SchedulerPool
}