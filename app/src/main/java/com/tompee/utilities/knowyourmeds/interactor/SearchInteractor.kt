package com.tompee.utilities.knowyourmeds.interactor

import com.tompee.utilities.knowyourmeds.base.BaseInteractor
import com.tompee.utilities.knowyourmeds.core.api.MedApi
import com.tompee.utilities.knowyourmeds.core.preferences.Preferences
import com.tompee.utilities.knowyourmeds.model.Medicine2
import com.tompee.utilities.knowyourmeds.model.MedicineContainer
import com.tompee.utilities.knowyourmeds.model.Type
import io.reactivex.Observable
import io.reactivex.Single

class SearchInteractor(private val preferences: Preferences,
                       private val medApi: MedApi,
                       private val medicineContainer: MedicineContainer) : BaseInteractor {

    fun getIsDisplayDisclaimer(): Single<Boolean> = Single.just(preferences.isShowDisclaimer())

    fun getIsDueForRater(): Single<Boolean> = Single.just(preferences.isDueForRater())
            .doOnSuccess { preferences.incrementRaterCount() }

    fun getMedicine(name: String): Single<List<Medicine2>> {
        return Single.just(Medicine2())
                .getRxNormId(name)
                .getPropertyName()
                .getPrescribable()
                .getTty()
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

    private fun Single<Medicine2>.getRxNormId(name: String): Single<Medicine2> {
        return this.flatMap { medicine ->
            medApi.getRxNormId(name)
                    .doOnSuccess { medicine.normId = it.idGroup?.rxnormId!![0] }
                    .map { medicine }
        }
    }

    private fun Single<Medicine2>.getPropertyName(): Single<Medicine2> {
        return this.flatMap { medicine ->
            medApi.getPropertyName(medicine.normId)
                    .doOnSuccess { medicine.name = it.propConceptGroup?.propConcept!![0].propValue!! }
                    .map { medicine }
                    .onErrorResumeNext(Single.just(medicine))
        }
    }

    private fun Single<Medicine2>.getPrescribable(): Single<Medicine2> {
        return this.flatMap { medicine ->
            medApi.getPrescribable(medicine.normId)
                    .doOnSuccess { medicine.isPrescribable = it.propConceptGroup?.propConcept!![0].propValue!! == "Y" }
                    .map { medicine }
                    .onErrorResumeNext(Single.just(medicine))
        }
    }

    private fun Single<Medicine2>.getTty(): Single<Medicine2> {
        return this.flatMap { medicine ->
            medApi.getTty(medicine.normId)
                    .doOnSuccess { medicine.tty = Type.getType(it.propConceptGroup?.propConcept!![0].propValue!!) }
                    .map { medicine }
                    .onErrorResumeNext(Single.just(medicine))
        }
    }
}