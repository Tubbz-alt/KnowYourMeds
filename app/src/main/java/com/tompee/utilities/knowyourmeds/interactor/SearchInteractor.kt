package com.tompee.utilities.knowyourmeds.interactor

import com.tompee.utilities.knowyourmeds.base.BaseInteractor
import com.tompee.utilities.knowyourmeds.core.api.MedApi
import com.tompee.utilities.knowyourmeds.core.preferences.Preferences
import com.tompee.utilities.knowyourmeds.model.Medicine
import com.tompee.utilities.knowyourmeds.model.MedicineContainer
import com.tompee.utilities.knowyourmeds.repo.MedicineRepo
import io.reactivex.Observable
import io.reactivex.Single

class SearchInteractor(private val medApi: MedApi,
                       private val medicineRepo: MedicineRepo,
                       private val medicineContainer: MedicineContainer,
                       private val preferences: Preferences) : BaseInteractor {

    fun getIsDisplayDisclaimer(): Single<Boolean> = Single.just(preferences.isShowDisclaimer())

    fun getIsDueForRater(): Single<Boolean> = Single.just(preferences.isDueForRater())
            .doOnSuccess { preferences.incrementRaterCount() }

    fun searchMedicine(name: String): Single<List<Medicine>> =
            medicineRepo.getMedicine(name)
                    .doOnSuccess { medicineContainer.medicine = it }
                    .map { listOf(it) }
                    .onErrorResumeNext(
                            medApi.getSuggestions(name)
                                    .map { it.group?.suggestion?.list }
                                    .flatMap { list ->
                                        Observable.fromIterable(list)
                                                .map { name -> Medicine(name = name) }
                                                .toList()
                                    }
                                    .onErrorResumeNext(Single.just(listOf()))
                                    .doOnSuccess { medicineContainer.medicine = null }
                    )

    fun setDontShowDisclaimerNext() {
        preferences.showDisclaimerNext(false)
    }
}