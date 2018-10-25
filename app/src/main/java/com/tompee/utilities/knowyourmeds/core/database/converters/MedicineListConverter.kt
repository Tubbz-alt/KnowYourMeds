package com.tompee.utilities.knowyourmeds.core.database.converters

import androidx.room.TypeConverter
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.tompee.utilities.knowyourmeds.model.Medicine

class MedicineListConverter {

    private val gson = GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create()
    private val type = (object : TypeToken<List<Medicine>>() {}).type

    @TypeConverter
    fun toString(list: List<Medicine>): String = gson.toJson(list, type)

    @TypeConverter
    fun toList(value: String): List<Medicine> = gson.fromJson(value, type)
}