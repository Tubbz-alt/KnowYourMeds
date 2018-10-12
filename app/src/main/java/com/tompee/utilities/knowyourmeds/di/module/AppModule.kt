package com.tompee.utilities.knowyourmeds.di.module

import android.content.Context
import com.tompee.utilities.knowyourmeds.KnowYourMedsApplication
import com.tompee.utilities.knowyourmeds.core.api.MedApi
import com.tompee.utilities.knowyourmeds.model.MedicineContainer
import com.tompee.utilities.knowyourmeds.model.SchedulerPool
import com.tompee.utilities.knowyourmeds.repo.MedicineRepo
import com.tompee.utilities.knowyourmeds.repo.impl.MedicineRepoImpl
import dagger.Module
import dagger.Provides
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Singleton

@Module
class AppModule(private val application: KnowYourMedsApplication) {
    @Provides
    @Singleton
    fun provideApplication(): KnowYourMedsApplication = application

    @Provides
    @Singleton
    fun provideContext(): Context = application


    @Provides
    @Singleton
    fun provideSchedulerPool(): SchedulerPool = SchedulerPool(Schedulers.io(),
            AndroidSchedulers.mainThread(), Schedulers.computation(), Schedulers.trampoline())

    @Provides
    @Singleton
    fun provideMedicineContainer(): MedicineContainer = MedicineContainer()

    @Provides
    @Singleton
    fun provideMedicineRepo(medicineRepoImpl: MedicineRepoImpl): MedicineRepo = medicineRepoImpl

    @Provides
    @Singleton
    fun provideMedicineRepoImpl(medApi: MedApi): MedicineRepoImpl = MedicineRepoImpl(medApi)
}