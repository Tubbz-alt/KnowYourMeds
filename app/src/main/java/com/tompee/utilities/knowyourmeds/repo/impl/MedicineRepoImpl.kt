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
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class MedicineRepoImpl(private val medApi: MedApi,
                       private val dailyMedApi: DailyMedApi,
                       private val medlineApi: MedlineApi,
                       private val medicineDao: MedicineDao) : MedicineRepo {

    override fun getMedicine(name: String): Single<Medicine> {
        return medApi.getRxNormId(name)
                .map { it.group?.idList!![0] }
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

    override fun deleteMedicine(medicine: Medicine): Completable {
        return medicineDao.deleteMedicine(medicine.convertToEntity())
                .ignoreElement()
    }

    override fun getProperties(id: String): Single<List<String>> {
        return medicineDao.getMedicineFrom(id)
                .map { it.ingredientList }
                .filter { it.isNotEmpty() }
                .toSingle()
                .onErrorResumeNext(searchIngredientsFromNetwork(id)
                        .doOnSuccess { /*medicineDao.updateIngredients(id, it)*/ })
    }

    override fun getMarketDrugs(id: String): Single<List<MarketDrug>> {
        return searchMarketDrugs(id)
                .flatMap { list ->
                    Observable.fromIterable(list)
                            .map { it.convertToEntity(id) }
                            .toList()
                            .doOnSuccess { medicineDao.insertMarketDrug(it) }
                            .map { list }
                }
    }

    override fun getCachedMarketDrugs(id: String): Single<List<MarketDrug>> {
        return medicineDao.getMarketDrug(id)
                .filter { it.isNotEmpty() }
                .toSingle()
                .convertToMarketDrug()
    }

    override fun getMedicineType(id: String, type: MedicineType): Single<List<Medicine>> {
        return searchTtyValues(id, type.tag)
                .flatMap { list ->
                    Observable.fromIterable(list)
                            .map { it.convertToTypeEntity(id) }
                            .toList()
                            .doOnSuccess { medicineDao.insertType(it) }
                            .map { list }
                }
    }

    override fun getCachedMedicineType(id: String, type: MedicineType): Single<List<Medicine>> {
        return medicineDao.getMedicineType(id, type)
                .filter { it.isNotEmpty() }
                .toSingle()
                .convertToMedicine()
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
        return searchInteractions(id)
                .flatMap { list ->
                    Observable.fromIterable(list)
                            .map { it.convertToEntity(id) }
                            .toList()
                            .doOnSuccess { medicineDao.insertInteraction(it) }
                            .map { list }
                }
    }

    override fun getCachedInteractions(id: String): Single<List<InteractionPair>> {
        return medicineDao.getInteractions(id)
                .filter { it.isNotEmpty() }
                .toSingle()
                .convertToPair()
    }

    override fun getMedicineList(): Observable<List<Medicine>> =
            medicineDao.getMedicines()
                    .convertToMedicine()

    //region Extensions
    private fun Observable<List<MedicineEntity>>.convertToMedicine(): Observable<List<Medicine>> {
        return this.flatMap { list ->
            Observable.fromIterable(list)
                    .map { it.convertToMedicine() }
                    .toList()
                    .toObservable()
        }
    }

    private fun Single<List<MarketDrugEntity>>.convertToMarketDrug(): Single<List<MarketDrug>> {
        return this.flatMap { list ->
            Observable.fromIterable(list)
                    .map { it.convertToMarketDrug() }
                    .toList()
        }
    }

    private fun Single<List<TypeEntity>>.convertToMedicine(): Single<List<Medicine>> {
        return this.flatMap { list ->
            Observable.fromIterable(list)
                    .map { it.convertToMedicine() }
                    .toList()
        }
    }

    private fun Single<List<InteractionEntity>>.convertToPair(): Single<List<InteractionPair>> {
        return this.flatMap { list ->
            Observable.fromIterable(list)
                    .map { it.convertToPair() }
                    .toList()
        }
    }

    private fun Medicine.convertToEntity(): MedicineEntity =
            MedicineEntity(id, name, url, isPrescribable, type, ingredientList)

    private fun Medicine.convertToTypeEntity(medId: String): TypeEntity =
            TypeEntity(medId, type, id, name, isPrescribable)

    private fun MarketDrug.convertToEntity(medId: String): MarketDrugEntity =
            MarketDrugEntity(medId, name, setId, version, publishedDate)

    private fun InteractionPair.convertToEntity(medId: String): InteractionEntity =
            InteractionEntity(medId, source, sourceUrl, partner, partnerUrl, interaction)
    //endregion

    //region Network
    private fun searchMedicineFromNetwork(name: String): Single<Medicine> {
        fun Single<Medicine>.getRxNormId(name: String): Single<Medicine> {
            return this.flatMap { medicine ->
                medApi.getRxNormId(name)
                        .doOnSuccess { medicine.id = it.group?.idList!![0] }
                        .map { medicine }
            }
        }

        fun Single<Medicine>.getMedicineName(): Single<Medicine> {
            return this.flatMap { medicine ->
                medApi.getName(medicine.id)
                        .doOnSuccess { medicine.name = it.group?.list!![0].value }
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
                .map { it.relatedGroup?.conceptGroupList!! }
                .flatMap { conceptGroupList ->
                    Observable.fromIterable(conceptGroupList)
                            .map { it.propertyList }
                            .flatMap { propertyList ->
                                Observable.fromIterable(propertyList)
                                        .map { it.name }
                            }
                            .toList()
                }
                .onErrorResumeNext(Single.just(listOf()))
    }

    private fun searchMarketDrugs(id: String): Single<List<MarketDrug>> {
        fun iterateData(data: List<Data>): Observable<MarketDrug> {
            return Observable.fromIterable(data)
                    .map { MarketDrug(it.title, it.setId, it.version, it.date) }
        }

        return dailyMedApi.getSpl(id, 1)
                .map { it.metadata }
                .flatMap { metadata ->
                    Observable.range(metadata.current, metadata.total)
                            .flatMap { page ->
                                dailyMedApi.getSpl(id, page)
                                        .flatMapObservable { splModel -> iterateData(splModel.dataList) }
                            }
                            .toList()
                }
                .onErrorResumeNext(Single.just(listOf()))
    }

    private fun searchTtyValues(id: String, type: String): Single<List<Medicine>> {
        return medApi.getTtyValues(id, type)
                .map { it.relatedGroup?.conceptGroupList!![0].propertyList }
                .flatMap { list ->
                    Observable.fromIterable(list)
                            .concatMapSingle { prop ->
                                Single.just(Medicine())
                                        .doOnSuccess {
                                            it.name = prop.name
                                            it.id = prop.id
                                        }
                                        .getAttributes()
                            }
                            .toList()
                }
    }

    private fun searchUrl(id: String): Single<String> {
        return medlineApi.getMedlineUrl(id)
                .map { it.feed?.entryList!![0].linkList[0].url }
    }

    private fun searchInteractions(id: String): Single<List<InteractionPair>> {
        return medApi.getInteraction(id)
                .flatMapObservable { model ->
                    Observable.fromIterable(model.typeGroupList)
                            .flatMap { groupList ->
                                Observable.fromIterable(groupList.typeList)
                                        .map { it.pairList }
                                        .concatMap { pairList ->
                                            Observable.fromIterable(pairList)
                                                    .map {
                                                        val source = it.conceptList[0].source?.name
                                                                ?: ""
                                                        val sourceUrl = it.conceptList[0].source?.url
                                                                ?: ""
                                                        val partner = it.conceptList[1].source?.name
                                                                ?: ""
                                                        val partnerUrl = it.conceptList[1].source?.url
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
                    .map { it.group?.list }
                    .flatMap { propConceptList ->
                        Observable.fromIterable(propConceptList)
                                .doOnNext { propConcept ->
                                    when (propConcept.name) {
                                        Attributes.TTY.name -> medicine.type = MedicineType[propConcept.value]
                                        Attributes.PRESCRIBABLE.name -> medicine.isPrescribable = propConcept.value == "Y"
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