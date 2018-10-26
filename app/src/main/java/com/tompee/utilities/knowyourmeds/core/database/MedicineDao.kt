package com.tompee.utilities.knowyourmeds.core.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tompee.utilities.knowyourmeds.core.database.entities.InteractionEntity
import com.tompee.utilities.knowyourmeds.core.database.entities.MarketDrugEntity
import com.tompee.utilities.knowyourmeds.core.database.entities.MedicineEntity
import com.tompee.utilities.knowyourmeds.core.database.entities.TypeEntity
import com.tompee.utilities.knowyourmeds.model.MedicineType
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface MedicineDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(medicine: MedicineEntity)

    @Query("SELECT * FROM medicine WHERE id = :id")
    fun getMedicineFrom(id: String): Single<MedicineEntity>

    @Query("UPDATE medicine SET url = :url WHERE id = :id")
    fun updateUrl(id: String, url: String)

    @Query("UPDATE medicine SET ingredients = :ingredients WHERE id = :id")
    fun updateIngredients(id: String, ingredients: List<String>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMarketDrug(marketDrugEntityList: List<MarketDrugEntity>)

    @Query("SELECT * FROM market_drug WHERE medId = :id")
    fun getMarketDrug(id: String): Single<List<MarketDrugEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertType(typeEntityList: List<TypeEntity>)

    @Query("SELECT * FROM medicine_type WHERE medId = :id  AND type = :type")
    fun getMedicineType(id: String, type: MedicineType): Single<List<TypeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertInteraction(interactionEntityList: List<InteractionEntity>)

    @Query("SELECT * FROM interactions WHERE medId = :id")
    fun getInteractions(id: String): Single<List<InteractionEntity>>

    @Query("SELECT * FROM medicine ")
    fun getMedicines(): Observable<List<MedicineEntity>>

    @Delete
    fun deleteMedicine(medicineEntity: MedicineEntity): Single<Int>
}