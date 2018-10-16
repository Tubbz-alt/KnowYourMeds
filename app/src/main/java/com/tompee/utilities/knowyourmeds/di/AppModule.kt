package com.tompee.utilities.knowyourmeds.di

import android.app.Application
import android.content.Context
import com.tompee.utilities.knowyourmeds.model.MedicineContainer
import com.tompee.utilities.knowyourmeds.model.SchedulerPool
import dagger.Module
import dagger.Provides
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Singleton

@Module
class AppModule {
    @Provides
    @Singleton
    fun provideContext(application: Application): Context = application.applicationContext

    @Provides
    @Singleton
    fun provideSchedulerPool(): SchedulerPool = SchedulerPool(Schedulers.io(),
            AndroidSchedulers.mainThread(), Schedulers.computation(), Schedulers.trampoline())

    @Provides
    @Singleton
    fun provideMedicineContainer(): MedicineContainer = MedicineContainer()
}