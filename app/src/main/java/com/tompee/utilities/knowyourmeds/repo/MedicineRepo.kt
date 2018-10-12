package com.tompee.utilities.knowyourmeds.repo

import com.tompee.utilities.knowyourmeds.model.Medicine2
import io.reactivex.Completable
import io.reactivex.Single

interface MedicineRepo {
    fun searchMedicine(name: String): Single<Medicine2>

    fun populateMedicineInfo(medicine: Medicine2): Completable
}