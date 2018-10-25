package com.tompee.utilities.knowyourmeds.di

import android.app.Application
import com.tompee.utilities.knowyourmeds.KnowYourMedsApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    CoreModule::class,
    RepoModule::class,
    ActivityModule::class,
    AndroidSupportInjectionModule::class
])
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(knowYourMedsApplication: KnowYourMedsApplication)
}