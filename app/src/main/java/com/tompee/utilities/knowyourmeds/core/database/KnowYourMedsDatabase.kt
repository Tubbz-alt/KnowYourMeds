package com.tompee.utilities.knowyourmeds.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tompee.utilities.knowyourmeds.core.database.converters.MedicineTypeConverter
import com.tompee.utilities.knowyourmeds.core.database.converters.StringListConverter
import com.tompee.utilities.knowyourmeds.core.database.entities.InteractionEntity
import com.tompee.utilities.knowyourmeds.core.database.entities.MarketDrugEntity
import com.tompee.utilities.knowyourmeds.core.database.entities.MedicineEntity
import com.tompee.utilities.knowyourmeds.core.database.entities.TypeEntity

@Database(entities = [MedicineEntity::class, MarketDrugEntity::class, TypeEntity::class, InteractionEntity::class], version = 5, exportSchema = false)
@TypeConverters(MedicineTypeConverter::class, StringListConverter::class)
abstract class KnowYourMedsDatabase : RoomDatabase() {
    abstract fun medicineDao(): MedicineDao
}