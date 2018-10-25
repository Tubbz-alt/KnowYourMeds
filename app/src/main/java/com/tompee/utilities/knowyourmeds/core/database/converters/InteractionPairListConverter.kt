package com.tompee.utilities.knowyourmeds.core.database.converters

import androidx.room.TypeConverter
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.tompee.utilities.knowyourmeds.model.InteractionPair

class InteractionPairListConverter {

    private val gson = GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create()
    private val type = (object : TypeToken<List<InteractionPair>>() {}).type

    @TypeConverter
    fun toString(list: List<InteractionPair>): String = gson.toJson(list, type)

    @TypeConverter
    fun toList(value: String): List<InteractionPair> = gson.fromJson(value, type)
}