package com.tompee.utilities.knowyourmeds.di

import com.tompee.utilities.knowyourmeds.core.api.DailyMedApi
import com.tompee.utilities.knowyourmeds.core.api.MedApi
import com.tompee.utilities.knowyourmeds.core.api.MedlineApi
import com.tompee.utilities.knowyourmeds.core.preferences.Preferences
import com.tompee.utilities.knowyourmeds.repo.MedicineRepo
import com.tompee.utilities.knowyourmeds.repo.impl.MedicineRepoImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepoModule {
    @Provides
    @Singleton
    fun provideMedicineRepo(medicineRepoImpl: MedicineRepoImpl): MedicineRepo = medicineRepoImpl

    @Provides
    @Singleton
    fun provideMedicineRepoImpl(medApi: MedApi,
                                medlineApi: MedlineApi,
                                dailyMedApi: DailyMedApi,
                                preferences: Preferences): MedicineRepoImpl =
            MedicineRepoImpl(medApi, medlineApi, dailyMedApi, preferences)
}