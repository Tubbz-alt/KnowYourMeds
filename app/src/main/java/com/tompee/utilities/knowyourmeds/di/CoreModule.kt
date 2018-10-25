package com.tompee.utilities.knowyourmeds.di

import android.content.Context
import androidx.room.Room
import com.tompee.utilities.knowyourmeds.Constants
import com.tompee.utilities.knowyourmeds.core.api.DailyMedApi
import com.tompee.utilities.knowyourmeds.core.api.MedApi
import com.tompee.utilities.knowyourmeds.core.api.MedlineApi
import com.tompee.utilities.knowyourmeds.core.asset.AssetManager
import com.tompee.utilities.knowyourmeds.core.database.KnowYourMedsDatabase
import com.tompee.utilities.knowyourmeds.core.database.MedicineDao
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
    //region api
    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
    }

    @Provides
    @Singleton
    fun provideDailyMedApi(client: OkHttpClient): DailyMedApi {
        val retrofit = Retrofit.Builder()
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(Constants.DAILY_MED_BASE_URL)
                .client(client)
                .build()
        return retrofit.create(DailyMedApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMedApi(client: OkHttpClient): MedApi {
        val retrofit = Retrofit.Builder()
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(Constants.MED_BASE_URL)
                .client(client)
                .build()
        return retrofit.create(MedApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMedlineApi(client: OkHttpClient): MedlineApi {
        val retrofit = Retrofit.Builder()
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(Constants.MEDLINE_BASE_URL)
                .client(client)
                .build()
        return retrofit.create(MedlineApi::class.java)
    }

    //endregion

    //region asset
    @Provides
    @Singleton
    fun provideAssetManager(context: Context): AssetManager = AssetManager(context)
    //endregion

    //region preferences
    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context): SharedPreferences = SharedPreferences(context)

    @Provides
    @Singleton
    fun providePreferences(sharedPreferences: SharedPreferences): Preferences = sharedPreferences
    //endregion

    //region database
    @Provides
    @Singleton
    fun provideDatabase(context: Context): KnowYourMedsDatabase {
        return Room.databaseBuilder(context, KnowYourMedsDatabase::class.java, Constants.DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()
    }

    @Provides
    @Singleton
    fun provideMedicineDao(database: KnowYourMedsDatabase): MedicineDao = database.medicineDao()
    //endregion
}