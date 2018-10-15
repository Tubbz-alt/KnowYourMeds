package com.tompee.utilities.knowyourmeds.interactor

import com.tompee.utilities.knowyourmeds.base.BaseInteractor
import com.tompee.utilities.knowyourmeds.core.api.MedApi
import com.tompee.utilities.knowyourmeds.core.preferences.Preferences
import com.tompee.utilities.knowyourmeds.model.Medicine2
import com.tompee.utilities.knowyourmeds.model.MedicineContainer
import com.tompee.utilities.knowyourmeds.repo.MedicineRepo
import io.reactivex.Observable
import io.reactivex.Single

class SearchInteractor(private val preferences: Preferences,
                       private val medApi: MedApi,
                       private val medicineRepo: MedicineRepo,
                       private val medicineContainer: MedicineContainer) : BaseInteractor {

    fun getIsDisplayDisclaimer(): Single<Boolean> = Single.just(preferences.isShowDisclaimer())

    fun getIsDueForRater(): Single<Boolean> = Single.just(preferences.isDueForRater())
            .doOnSuccess { preferences.incrementRaterCount() }

    fun getMedicine(name: String): Single<List<Medicine2>> = medicineRepo.searchMedicine(name)
            .doOnSuccess { medicineContainer.medicine = it }
            .map { listOf(it) }
            .onErrorResumeNext(
                    medApi.getSuggestions(name)
                            .map { it.suggestionGroup?.suggestionList?.suggestion }
                            .flatMap { list ->
                                Observable.fromIterable(list)
                                        .map { name -> Medicine2(name = name) }
                                        .toList()
                            }
                            .onErrorResumeNext(Single.just(listOf()))
                            .doOnSuccess { medicineContainer.medicine = null }
            )
}