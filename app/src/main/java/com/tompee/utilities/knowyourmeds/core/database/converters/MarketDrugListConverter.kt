package com.tompee.utilities.knowyourmeds.core.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tompee.utilities.knowyourmeds.model.MarketDrug

class MarketDrugListConverter {

    private val gson = Gson()
    private val type = (object : TypeToken<List<MarketDrug>>() {}).type

    @TypeConverter
    fun toString(list: List<MarketDrug>): String = gson.toJson(list, type)

    @TypeConverter
    fun toList(value: String): List<MarketDrug> = gson.fromJson(value, type)
}