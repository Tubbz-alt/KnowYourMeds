package com.tompee.utilities.knowyourmeds.core.database.converters

import android.arch.persistence.room.TypeConverter
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class StringListConverter {

    @TypeConverter
    fun toList(entry: String): List<String>? = jsonToList(entry)

    @TypeConverter
    fun toString(list: List<String>): String? = convertToJsonString(list)

    private fun convertToJsonString(list: List<String>): String? {
        return try {
            val json = JSONObject()
            json.put("list", JSONArray(list))
            return json.toString()
        } catch (e: JSONException) {
            null
        }
    }

    private fun jsonToList(string: String): List<String>? {
        return try {
            val json = JSONObject(string)
            val array = json.optJSONArray("list")
            val list = ArrayList<String>()
            for (index in 0 until array.length()) {
                list.add(array.optString(index))
            }
            return list
        } catch (e: JSONException) {
            null
        } catch (e: NullPointerException) {
            null
        }
    }
}