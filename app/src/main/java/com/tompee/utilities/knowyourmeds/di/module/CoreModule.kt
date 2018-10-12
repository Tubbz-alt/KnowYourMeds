package com.tompee.utilities.knowyourmeds.di.module

import android.content.Context
import com.tompee.utilities.knowyourmeds.Constants
import com.tompee.utilities.knowyourmeds.core.api.DailyMedApi
import com.tompee.utilities.knowyourmeds.core.api.MedApi
import com.tompee.utilities.knowyourmeds.core.api.MedlineApi
import com.tompee.utilities.knowyourmeds.core.asset.AssetManager
import com.tompee.utilities.knowyourmeds.core.preferences.Preferences
import com.tompee.utilities.knowyourmeds.core.preferences.shared.SharedPreferences
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import javax.inject.Singleton

@Module
class CoreModule {

    @Provides
    @Singleton
    fun providePreferences(sharedPreferences: SharedPreferences): Preferences = sharedPreferences

    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context): SharedPreferences = SharedPreferences(context)

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
                .baseUrl(Constants.MED_BASE_URL)
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
                .baseUrl(Constants.MEDLINE_BASE_URL)
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
                .baseUrl(Constants.DAILY_MED_BASE_URL)
                .client(httpClient)
                .build()
        return retrofit.create(DailyMedApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAssetManager(context: Context): AssetManager = AssetManager(context)
}