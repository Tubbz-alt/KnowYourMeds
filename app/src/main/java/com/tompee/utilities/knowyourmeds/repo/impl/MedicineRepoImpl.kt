package com.tompee.utilities.knowyourmeds.repo.impl

import com.tompee.utilities.knowyourmeds.core.api.Attributes
import com.tompee.utilities.knowyourmeds.core.api.DailyMedApi
import com.tompee.utilities.knowyourmeds.core.api.Data
import com.tompee.utilities.knowyourmeds.core.api.MedApi
import com.tompee.utilities.knowyourmeds.core.api.MedlineApi
import com.tompee.utilities.knowyourmeds.core.database.MedicineDao
import com.tompee.utilities.knowyourmeds.core.database.entities.InteractionEntity
import com.tompee.utilities.knowyourmeds.core.database.entities.MarketDrugEntity
import com.tompee.utilities.knowyourmeds.core.database.entities.MedicineEntity
import com.tompee.utilities.knowyourmeds.core.database.entities.TypeEntity
import com.tompee.utilities.knowyourmeds.model.Ingredient
import com.tompee.utilities.knowyourmeds.model.InteractionPair
import com.tompee.utilities.knowyourmeds.model.MarketDrug
import com.tompee.utilities.knowyourmeds.model.Medicine
import com.tompee.utilities.knowyourmeds.model.MedicineType
import com.tompee.utilities.knowyourmeds.repo.MedicineRepo
import io.reactivex.Observable
import io.reactivex.Single

class MedicineRepoImpl(private val medApi: MedApi,
                       private val dailyMedApi: DailyMedApi,
                       private val medlineApi: MedlineApi,
                       private val medicineDao: MedicineDao) : MedicineRepo {

    override fun getMedicine(name: String): Single<Medicine> {
        return medApi.getRxNormId(name)
                .map { it.idGroup?.rxnormId!![0] }
                .flatMap { id ->
                    medicineDao.getMedicineFrom(id)
                            .map { it.convertToMedicine() }
                            .onErrorResumeNext(
                                    searchMedicineFromNetwork(name)
                                            .flatMap { medicine ->
                                                Single.just(medicine)
                                                        .map { it.convertToEntity() }
                                                        .doOnSuccess(medicineDao::insert)
                                                        .map { medicine }
                                            })
                }
    }

    override fun getProperties(id: String): Single<List<String>> {
        return medicineDao.getMedicineFrom(id)
                .map { it.ingredientList }
                .filter { it.isNotEmpty() }
                .toSingle()
                .onErrorResumeNext(searchIngredientsFromNetwork(id)
                        .doOnSuccess { medicineDao.updateIngredients(id, it) })
    }

    override fun getMarketDrugs(id: String): Single<List<MarketDrug>> {
        return medicineDao.getMarketDrugFrom(id)
                .map { it.list }
                .onErrorResumeNext(searchMarketDrugs(id)
                        .doOnSuccess {
                            medicineDao.insert(MarketDrugEntity(0, id, it))
                        })
    }

    override fun getMedicineType(id: String, type: MedicineType): Single<List<Medicine>> {
        return medicineDao.getMedicineType(id, type)
                .map { it.list }
                .onErrorResumeNext(searchTtyValues(id, type.tag)
                        .doOnSuccess {
                            medicineDao.insert(TypeEntity(0, id, type, it))
                        })
    }

    override fun getUrl(id: String): Single<String> {
        return medicineDao.getMedicineFrom(id)
                .map { it.url }
                .filter { it.isNotEmpty() }
                .toSingle()
                .onErrorResumeNext(searchUrl(id)
                        .doOnSuccess { medicineDao.updateUrl(id, it) })
    }

    override fun getInteractions(id: String): Single<List<InteractionPair>> {
        return medicineDao.getInteractions(id)
                .map { it.list }
                .onErrorResumeNext(searchInteractions(id)
                        .doOnSuccess {
                            medicineDao.insert(InteractionEntity(0, id, it))
                        })
    }

    private fun Medicine.convertToEntity(): MedicineEntity =
            MedicineEntity(id, name, url, isPrescribable, type, ingredientList)

    //region Network
    private fun searchMedicineFromNetwork(name: String): Single<Medicine> {
        fun Single<Medicine>.getRxNormId(name: String): Single<Medicine> {
            return this.flatMap { medicine ->
                medApi.getRxNormId(name)
                        .doOnSuccess { medicine.id = it.idGroup?.rxnormId!![0] }
                        .map { medicine }
            }
        }

        fun Single<Medicine>.getMedicineName(): Single<Medicine> {
            return this.flatMap { medicine ->
                medApi.getName(medicine.id)
                        .doOnSuccess { medicine.name = it.propConceptGroup?.propConcept!![0].propValue }
                        .map { medicine }
                        .onErrorResumeNext(Single.just(medicine))
            }
        }

        return Single.just(Medicine())
                .getRxNormId(name)
                .getMedicineName()
                .getAttributes()
    }

    private fun searchIngredientsFromNetwork(id: String): Single<List<String>> {
        return medApi.getTtyValues(id, Ingredient.tag)
                .map { it.relatedGroup?.conceptGroup!! }
                .flatMap { conceptGroupList ->
                    Observable.fromIterable(conceptGroupList)
                            .map { it.conceptProperties }
                            .flatMap { conceptPropertiesList ->
                                Observable.fromIterable(conceptPropertiesList)
                                        .map { it.name }
                            }
                            .toList()
                }
                .onErrorResumeNext(Single.just(listOf()))
    }

    private fun searchMarketDrugs(id: String): Single<List<MarketDrug>> {
        fun iterateData(data: List<Data>): Observable<MarketDrug> {
            return Observable.fromIterable(data)
                    .map { MarketDrug(it.title, it.setid, it.spl_version, it.published_date) }
        }

        return dailyMedApi.getSpl(id, 1)
                .map { it.metadata }
                .flatMap { metadata ->
                    Observable.range(metadata.current_page, metadata.total_pages)
                            .flatMap { page ->
                                dailyMedApi.getSpl(id, page)
                                        .flatMapObservable { splModel -> iterateData(splModel.data) }
                            }
                            .toList()
                }
                .onErrorResumeNext(Single.just(listOf()))
    }

    private fun searchTtyValues(id: String, type: String): Single<List<Medicine>> {
        return medApi.getTtyValues(id, type)
                .map { it.relatedGroup?.conceptGroup!![0].conceptProperties }
                .flatMap { list ->
                    Observable.fromIterable(list)
                            .concatMapSingle { prop ->
                                Single.just(Medicine())
                                        .doOnSuccess {
                                            it.name = prop.name
                                            it.id = prop.rxcui
                                        }
                                        .getAttributes()
                            }
                            .toList()
                }
    }

    private fun searchUrl(id: String): Single<String> {
        return medlineApi.getMedlineUrl(id)
                .map { it.feed?.entry!![0].link[0].href }
    }

    private fun searchInteractions(id: String): Single<List<InteractionPair>> {
        return medApi.getInteraction(id)
                .flatMapObservable { model ->
                    Observable.fromIterable(model.interactionTypeGroup)
                            .flatMap { group ->
                                Observable.fromIterable(group.interactionType)
                                        .map { it.interactionPair }
                                        .concatMap { pairList ->
                                            Observable.fromIterable(pairList)
                                                    .map {
                                                        val source = it.interactionConcept[0].sourceConceptItem?.name
                                                                ?: ""
                                                        val sourceUrl = it.interactionConcept[0].sourceConceptItem?.url
                                                                ?: ""
                                                        val partner = it.interactionConcept[1].sourceConceptItem?.name
                                                                ?: ""
                                                        val partnerUrl = it.interactionConcept[1].sourceConceptItem?.url
                                                                ?: ""
                                                        val interaction = it.description
                                                        return@map InteractionPair(source, sourceUrl, partner, partnerUrl, interaction)
                                                    }
                                                    .filter { it.interaction.isNotEmpty() }
                                        }
                            }
                }.toList()
    }

    private fun Single<Medicine>.getAttributes(): Single<Medicine> {
        return this.flatMap { medicine ->
            medApi.getAttributes(medicine.id)
                    .map { it.propConceptGroup?.propConcept }
                    .flatMap { propConceptList ->
                        Observable.fromIterable(propConceptList)
                                .doOnNext { propConcept ->
                                    when (propConcept.propName) {
                                        Attributes.TTY.name -> medicine.type = MedicineType[propConcept.propValue]
                                        Attributes.PRESCRIBABLE.name -> medicine.isPrescribable = propConcept.propValue == "Y"
                                    }
                                }
                                .toList()
                    }
                    .map { medicine }
                    .onErrorResumeNext(Single.just(medicine))
        }
    }
//endregion
}