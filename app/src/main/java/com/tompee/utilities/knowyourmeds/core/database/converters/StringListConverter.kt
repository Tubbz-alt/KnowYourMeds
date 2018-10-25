package com.tompee.utilities.knowyourmeds.core.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class StringListConverter {

    private val gson = Gson()
    private val type = (object : TypeToken<List<String>>() {}).type

    @TypeConverter
    fun toString(list: List<String>): String = gson.toJson(list, type)

    @TypeConverter
    fun toList(value: String): List<String> = gson.fromJson(value, type)
}