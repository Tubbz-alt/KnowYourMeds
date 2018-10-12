package com.tompee.utilities.knowyourmeds.interactor

import com.tompee.utilities.knowyourmeds.base.BaseInteractor
import com.tompee.utilities.knowyourmeds.model.Medicine2
import com.tompee.utilities.knowyourmeds.model.MedicineContainer
import com.tompee.utilities.knowyourmeds.repo.MedicineRepo
import io.reactivex.Single

class DetailInteractor(private val medicineRepo: MedicineRepo,
                       private val medicineContainer: MedicineContainer) : BaseInteractor {

    fun getMedicineInfo(): Single<Medicine2> {
        return medicineRepo.populateMedicineInfo(medicineContainer.medicine!!)
                .toSingle { medicineContainer.medicine!! }
    }

    fun getStockMedicine(): Single<Medicine2> {
        return Single.just(medicineContainer.medicine)
    }
}