package com.tompee.utilities.knowyourmeds.di.module

import android.content.Context
import com.tompee.utilities.knowyourmeds.KnowYourMedsApplication
import com.tompee.utilities.knowyourmeds.core.api.DailyMedApi
import com.tompee.utilities.knowyourmeds.core.api.MedApi
import com.tompee.utilities.knowyourmeds.core.api.MedlineApi
import com.tompee.utilities.knowyourmeds.core.asset.AssetManager
import com.tompee.utilities.knowyourmeds.core.preferences.Preferences
import com.tompee.utilities.knowyourmeds.core.preferences.shared.SharedPreferences
import com.tompee.utilities.knowyourmeds.model.MedicineContainer
import com.tompee.utilities.knowyourmeds.model.SchedulerPool
import dagger.Module
import dagger.Provides
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
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
    fun providePreferences(sharedPreferences: SharedPreferences): Preferences = sharedPreferences

    @Provides
    @Singleton
    fun provideSharedPreferences(): SharedPreferences = SharedPreferences(application)

    @Provides
    @Singleton
    fun provideSchedulerPool(): SchedulerPool = SchedulerPool(Schedulers.io(),
            AndroidSchedulers.mainThread(), Schedulers.computation(), Schedulers.trampoline())

    @Provides
    @Singleton
    fun provideApi(): MedApi {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

        val retrofit = Retrofit.Builder()
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("https://rxnav.nlm.nih.gov/REST/")
                .client(httpClient)
                .build()
        return retrofit.create(MedApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMedlineApi(): MedlineApi {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

        val retrofit = Retrofit.Builder()
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("https://apps.nlm.nih.gov/")
                .client(httpClient)
                .build()
        return retrofit.create(MedlineApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDailyMedApi(): DailyMedApi {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

        val retrofit = Retrofit.Builder()
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("https://dailymed.nlm.nih.gov/dailymed/services/v2/")
                .client(httpClient)
                .build()
        return retrofit.create(DailyMedApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMedicineContainer(): MedicineContainer = MedicineContainer()

    @Provides
    @Singleton
    fun provideAssetManager(): AssetManager = AssetManager(application)
}