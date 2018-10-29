package com.tompee.utilities.knowyourmeds.interactor

import com.tompee.utilities.knowyourmeds.base.BaseInteractor
import com.tompee.utilities.knowyourmeds.model.InteractionPair
import com.tompee.utilities.knowyourmeds.model.MarketDrug
import com.tompee.utilities.knowyourmeds.model.Medicine
import com.tompee.utilities.knowyourmeds.model.MedicineContainer
import com.tompee.utilities.knowyourmeds.model.MedicineType
import com.tompee.utilities.knowyourmeds.repo.MedicineRepo
import io.reactivex.Single

class DetailInteractor(private val medicineContainer: MedicineContainer,
                       private val medicineRepo: MedicineRepo) : BaseInteractor {

    fun getStockMedicine(): Single<Medicine> = Single.just(medicineContainer.medicine)

    fun getDetailedInfo(): Single<Medicine> =
            Single.just(medicineContainer.medicine!!)
                    .flatMap { medicine ->
                        medicineRepo.getProperties(medicine.id)
                                .doOnSuccess { medicine.ingredientList = it }
                                .map { medicine }
                                .onErrorResumeNext(Single.just(medicine))
                    }.flatMap { medicine ->
                        medicineRepo.getUrl(medicine.id)
                                .doOnSuccess { medicine.url = it }
                                .map { medicine }
                                .onErrorResumeNext(Single.just(medicine))
                    }

    fun getMarketDrugs(): Single<List<MarketDrug>> = medicineRepo.getMarketDrugs(medicineContainer.medicine!!.id)

    fun getCachedMarketDrugs(): Single<List<MarketDrug>> = medicineRepo.getCachedMarketDrugs(medicineContainer.medicine!!.id)

    fun getCachedMedicineType(type: MedicineType): Single<List<Medicine>> = medicineRepo.getCachedMedicineType(medicineContainer.medicine!!.id, type)

    fun getMedicineType(type: MedicineType): Single<List<Medicine>> = medicineRepo.getMedicineType(medicineContainer.medicine!!.id, type)

    fun getInteractions(): Single<List<InteractionPair>> = medicineRepo.getInteractions(medicineContainer.medicine!!.id)

    fun getCachedInteractions(): Single<List<InteractionPair>> = medicineRepo.getCachedInteractions(medicineContainer.medicine!!.id)

    fun addToFavorites() = medicineRepo.setFavorite(medicineContainer.medicine!!.id, true)

    fun removeFromFavorites() = medicineRepo.setFavorite(medicineContainer.medicine!!.id, false)
}