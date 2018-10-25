package com.tompee.utilities.knowyourmeds.core.database.converters

import androidx.room.TypeConverter
import com.tompee.utilities.knowyourmeds.model.MedicineType

class MedicineTypeConverter {

    @TypeConverter
    fun toString(type: MedicineType): String = type.tag

    @TypeConverter
    fun toType(value: String): MedicineType = MedicineType[value]
}
