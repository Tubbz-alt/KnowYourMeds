package com.tompee.utilities.knowyourmeds.repo.impl

import com.tompee.utilities.knowyourmeds.core.api.DailyMedApi
import com.tompee.utilities.knowyourmeds.core.api.Data
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
import com.tompee.utilities.knowyourmeds.model.MarketDrug
import com.tompee.utilities.knowyourmeds.model.Medicine2
import com.tompee.utilities.knowyourmeds.model.Type
import com.tompee.utilities.knowyourmeds.repo.MedicineRepo
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import java.util.*

class MedicineRepoImpl(private val medApi: MedApi,
                       private val medlineApi: MedlineApi,
                       private val dailyMedApi: DailyMedApi,
                       private val preferences: Preferences) : MedicineRepo {

    override fun searchMedicine(name: String): Single<Medicine2> {
        /* for now, search network */
        return searchMedicineFromNetwork(name)
    }

    override fun populateMedicineInfo(medicine: Medicine2): Completable {
        // for now, populate from network
        return populateMedicineInfoFromNetwork(medicine)
    }

    //region Network
    private fun searchMedicineFromNetwork(name: String): Single<Medicine2> {
        fun Single<Medicine2>.getRxNormId(name: String): Single<Medicine2> {
            return this.flatMap { medicine ->
                medApi.getRxNormId(name)
                        .doOnSuccess { medicine.normId = it.idGroup?.rxnormId!![0] }
                        .map { medicine }
            }
        }

        fun Single<Medicine2>.getMedicineName(): Single<Medicine2> {
            return this.flatMap { medicine ->
                medApi.getName(medicine.normId)
                        .doOnSuccess { medicine.name = it.propConceptGroup?.propConcept!![0].propValue }
                        .map { medicine }
                        .onErrorResumeNext(Single.just(medicine))
            }
        }

        fun Single<Medicine2>.getAttributes(): Single<Medicine2> {
            return this.flatMap { medicine ->
                medApi.getAttributes(medicine.normId)
                        .map { it.propConceptGroup?.propConcept }
                        .flatMap { propConceptList ->
                            Observable.fromIterable(propConceptList)
                                    .doOnNext { propConcept ->
                                        when (propConcept.propName) {
                                            "TTY" -> medicine.tty = Type.getType(propConcept.propValue)
                                            "PRESCRIBABLE" -> medicine.isPrescribable = propConcept.propValue == "Y"
                                        }
                                    }
                                    .toList()
                        }
                        .map { medicine }
                        .onErrorResumeNext(Single.just(medicine))
            }
        }

        return Single.just(Medicine2())
                .getRxNormId(name)
                .getMedicineName()
                .getAttributes()
    }

    private fun populateMedicineInfoFromNetwork(medicine: Medicine2): Completable {
        fun Single<Medicine2>.getUrl(): Single<Medicine2> {
            return this.flatMap { medicine ->
                medlineApi.getMedlineUrl(medicine.normId)
                        .doOnSuccess { medicine.url = it?.feed?.entry!![0].link[0].href }
                        .map { medicine }
                        .onErrorResumeNext(Single.just(medicine))
            }
        }

        fun Single<Medicine2>.getSources(): Single<Medicine2> {
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

        fun Single<Medicine2>.getSpl(): Single<Medicine2> {
            fun iterateData(data: List<Data>, medicine: Medicine2): Single<List<Data>> {
                return Observable.fromIterable(data)
                        .doOnNext {
                            val marketDrug = MarketDrug(it.title, it.setid, it.spl_version, it.published_date)
                            medicine.marketDrugList.add(marketDrug)
                        }
                        .toList()
            }

            return if (preferences.isSplEnabled()) {
                this.flatMap { medicine ->
                    dailyMedApi.getSpl(medicine.normId, 1)
                            .doOnSuccess { medicine.marketDrugList = mutableListOf() }
                            .flatMap { splModel ->
                                iterateData(splModel.data, medicine)
                                        .map { splModel.metadata!! }
                            }
                            .flatMap { metadata ->
                                Observable.range(metadata.current_page + 1, metadata.total_pages - 1)
                                        .doOnNext { page ->
                                            dailyMedApi.getSpl(medicine.normId, page)
                                                    .flatMap { splModel -> iterateData(splModel.data, medicine) }
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

        fun Single<Medicine2>.getTtyValues(): Single<Medicine2> {
            return this.flatMap { medicine ->
                medApi.getTtyValues(medicine.normId)
                        .map { it.relatedGroup?.conceptGroup!! }
                        .flatMap { conceptGroupList ->
                            Observable.fromIterable(conceptGroupList)
                                    .doOnNext { conceptGroup ->
                                        when {
                                            conceptGroup.tty == Brand.tag -> medicine.brands = conceptGroup.conceptProperties.map { it.name }
                                            conceptGroup.tty == Ingredient.tag -> medicine.ingredients = conceptGroup.conceptProperties.map { it.name }
                                            conceptGroup.tty == ClinicalDrugComponent.tag -> medicine.scdc = conceptGroup.conceptProperties.map { it.name }
                                            conceptGroup.tty == BrandedDrugComponent.tag -> medicine.sbdc = conceptGroup.conceptProperties.map { it.name }
                                            conceptGroup.tty == BrandedDoseFormGroup.tag -> medicine.sbdg = conceptGroup.conceptProperties.map { it.name }
                                            conceptGroup.tty == ClinicalDrugPack.tag -> medicine.scd = conceptGroup.conceptProperties.map { it.name }
                                            conceptGroup.tty == ClinicalDoseFormGroup.tag -> medicine.scdg = conceptGroup.conceptProperties.map { it.name }
                                            conceptGroup.tty == BrandedDrugPack.tag -> medicine.sbd = conceptGroup.conceptProperties.map { it.name }
                                        }
                                    }
                                    .toList()
                        }
                        .map { medicine }
                        .onErrorResumeNext(Single.just(medicine))
            }
        }

        fun Single<Medicine2>.getInteractions(): Single<Medicine2> {
            return this.flatMap { medicine ->
                medApi.getInteraction(medicine.normId)
                        .flatMap { model ->
                            Observable.fromIterable(model.interactionTypeGroup)
                                    .flatMap { group ->
                                        Observable.fromIterable(group.interactionType)
                                                .map { type ->
                                                    /* Refrain from using observable here */
                                                    val innerMap = mutableMapOf<String, String>()
                                                    var key = ""
                                                    type.interactionPair.forEach { pair ->
                                                        key = pair.interactionConcept[0].sourceConceptItem?.name!!
                                                        innerMap[pair.interactionConcept[1].sourceConceptItem?.name!!] = pair.description
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
        return Single.just(medicine)
                .doOnSuccess { it.date = Calendar.getInstance().time }
                .getUrl()
                .getSources()
                .getSpl()
                .getTtyValues()
                .getInteractions()
                .ignoreElement()
    }

    //endregion
}