package com.tompee.utilities.knowyourmeds.repo

import com.tompee.utilities.knowyourmeds.model.InteractionPair
import com.tompee.utilities.knowyourmeds.model.MarketDrug
import com.tompee.utilities.knowyourmeds.model.Medicine
import com.tompee.utilities.knowyourmeds.model.MedicineType
import io.reactivex.Single

interface MedicineRepo {
    fun getMedicine(name: String): Single<Medicine>

    fun getProperties(id: String): Single<List<String>>

    fun getMarketDrugs(id: String): Single<List<MarketDrug>>

    fun getCachedMarketDrugs(id: String): Single<List<MarketDrug>>

    fun getMedicineType(id: String, type: MedicineType): Single<List<Medicine>>

    fun getCachedMedicineType(id: String, type: MedicineType): Single<List<Medicine>>

    fun getUrl(id: String): Single<String>

    fun getInteractions(id: String): Single<List<InteractionPair>>

    fun getCachedInteractions(id: String): Single<List<InteractionPair>>
}