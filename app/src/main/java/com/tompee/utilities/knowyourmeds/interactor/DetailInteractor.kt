package com.tompee.utilities.knowyourmeds.interactor

import com.tompee.utilities.knowyourmeds.base.BaseInteractor
import com.tompee.utilities.knowyourmeds.core.api.DailyMedApi
import com.tompee.utilities.knowyourmeds.core.api.MedApi
import com.tompee.utilities.knowyourmeds.core.api.MedlineApi
import com.tompee.utilities.knowyourmeds.core.preferences.Preferences
import com.tompee.utilities.knowyourmeds.model.Brand
import com.tompee.utilities.knowyourmeds.model.BrandedDoseFormGroup
import com.tompee.utilities.knowyourmeds.model.BrandedDrugComponent
import com.tompee.utilities.knowyourmeds.model.BrandedDrugPack
import com.tompee.utilities.knowyourmeds.model.ClinicalDoseFormGroup
import com.tompee.utilities.knowyourmeds.model.ClinicalDrugComponent
import com.tompee.utilities.knowyourmeds.model.ClinicalDrugPack
import com.tompee.utilities.knowyourmeds.model.Ingredient
import com.tompee.utilities.knowyourmeds.model.Medicine2
import com.tompee.utilities.knowyourmeds.model.MedicineContainer
import io.reactivex.Observable
import io.reactivex.Single
import java.util.*

class DetailInteractor(private val medApi: MedApi,
                       private val medlineApi: MedlineApi,
                       private val dailyMedApi: DailyMedApi,
                       private val preferences: Preferences,
                       private val medicineContainer: MedicineContainer) : BaseInteractor {

    fun getMedicine(): Single<Medicine2> {
        return Single.just(medicineContainer.medicine!!)
                .doOnSuccess { it.date = Calendar.getInstance().time }
                .getUrl()
                .getSources()
                .getSpl()
                .getTtyValues()
                .getInteractions()
    }

    fun getStockMedicine(): Single<Medicine2> {
        return Single.just(medicineContainer.medicine)
    }

    private fun Single<Medicine2>.getUrl(): Single<Medicine2> {
        return this.flatMap { medicine ->
            medlineApi.getMedlineUrl(medicine.normId)
                    .doOnSuccess { medicine.url = it?.feed?.entry!![0].link!![0].href ?: "" }
                    .map { medicine }
                    .onErrorResumeNext(Single.just(medicine))
        }
    }

    private fun Single<Medicine2>.getSources(): Single<Medicine2> {
        return this.flatMap { medicine ->
            medApi.getSources(medicine.normId)
                    .map { it.propConceptGroup?.propConcept }
                    .flatMap { propConcept ->
                        Observable.fromIterable(propConcept)
                                .map { it.propValue }
                                .toList()
                    }
                    .doOnSuccess { medicine.sources = it }
                    .map { medicine }
                    .onErrorResumeNext(Single.just(medicine))
        }
    }

    private fun Single<Medicine2>.getSpl(): Single<Medicine2> {
        return if (preferences.isSplEnabled()) {
            this.flatMap { medicine ->
                dailyMedApi.getSpl(medicine.normId, 1)
                        .flatMap { splModel ->
                            Observable.fromIterable(splModel.data!!)
                                    .doOnNext { medicine.spls[it.setid!!] = it.title!! }
                                    .toList()
                                    .map { splModel.metadata!! }
                        }
                        .flatMap { metadata ->
                            Observable.range(metadata.current_page + 1, metadata.total_pages - 1)
                                    .doOnNext { page ->
                                        dailyMedApi.getSpl(medicine.normId, page)
                                                .flatMap { splModel ->
                                                    Observable.fromIterable(splModel.data!!)
                                                            .doOnNext { medicine.spls[it.setid!!] = it.title!! }
                                                            .toList()
                                                            .map { splModel.metadata!! }
                                                }
                                    }
                                    .toList()
                        }
                        .map { medicine }
                        .onErrorResumeNext(Single.just(medicine))
            }
        } else {
            this
        }
    }

    private fun Single<Medicine2>.getTtyValues(): Single<Medicine2> {
        return this.flatMap { medicine ->
            medApi.getTtyValues(medicine.normId)
                    .map { it.relatedGroup?.conceptGroup!! }
                    .flatMap { conceptGroupList ->
                        Observable.fromIterable(conceptGroupList)
                                .doOnNext { conceptGroup ->
                                    when {
                                        conceptGroup.tty == Brand.tag -> medicine.brands = conceptGroup.conceptProperties!!.map { it.name }
                                        conceptGroup.tty == Ingredient.tag -> medicine.ingredients = conceptGroup.conceptProperties!!.map { it.name }
                                        conceptGroup.tty == ClinicalDrugComponent.tag -> medicine.scdc = conceptGroup.conceptProperties!!.map { it.name }
                                        conceptGroup.tty == BrandedDrugComponent.tag -> medicine.sbdc = conceptGroup.conceptProperties!!.map { it.name }
                                        conceptGroup.tty == BrandedDoseFormGroup.tag -> medicine.sbdg = conceptGroup.conceptProperties!!.map { it.name }
                                        conceptGroup.tty == ClinicalDrugPack.tag -> medicine.scd = conceptGroup.conceptProperties!!.map { it.name }
                                        conceptGroup.tty == ClinicalDoseFormGroup.tag -> medicine.scdg = conceptGroup.conceptProperties!!.map { it.name }
                                        conceptGroup.tty == BrandedDrugPack.tag -> medicine.sbd = conceptGroup.conceptProperties!!.map { it.name }
                                    }
                                }
                                .toList()
                    }
                    .map { medicine }
                    .onErrorResumeNext(Single.just(medicine))
        }
    }

    private fun Single<Medicine2>.getInteractions(): Single<Medicine2> {
        return this.flatMap { medicine ->
            medApi.getInteraction(medicine.normId)
                    .flatMap { model ->
                        Observable.fromIterable(model.interactionTypeGroup!!)
                                .flatMap { group ->
                                    Observable.fromIterable(group.interactionType!!)
                                            .map { type ->
                                                /* Refrain from using observable here */
                                                val innerMap = mutableMapOf<String, String>()
                                                var key = ""
                                                type.interactionPair!!.forEach { pair ->
                                                    key = pair.interactionConcept!![0].sourceConceptItem?.name!!
                                                    innerMap[pair.interactionConcept!![1].sourceConceptItem?.name!!] = pair.description!!
                                                }
                                                return@map key to innerMap
                                            }
                                }.filter { it.second.isNotEmpty() }
                                .toMap({ it.first }) { it.second }
                    }
                    .map {
                        medicine.interactions = it
                    }
                    .map { medicine }
                    .onErrorResumeNext(Single.just(medicine))
        }
    }
}