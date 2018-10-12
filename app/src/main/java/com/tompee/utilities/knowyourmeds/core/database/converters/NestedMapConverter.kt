package com.tompee.utilities.knowyourmeds.core.database.converters

import android.arch.persistence.room.TypeConverter
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class NestedMapConverter {
    @TypeConverter
    fun toMap(string: String): Map<String, Map<String, String>> = jsonToRecursiveMap(string)

    @TypeConverter
    fun toString(map: Map<String, Map<String, String>>): String = convertRecursiveMapToJsonString(map)

    private fun convertRecursiveMapToJsonString(map: Map<String, Map<String, String>>): String {
        val json = JSONObject()
        return try {
            json.put("map", JSONObject(map))
            return json.toString()
        } catch (e: JSONException) {
            "{}"
        }
    }

    private fun jsonToRecursiveMap(string: String): LinkedHashMap<String, Map<String, String>> {
        val retMap = LinkedHashMap<String, Map<String, String>>()
        try {
            val json = JSONObject(string).getJSONObject("map")
            val keysItr = json.keys()
            while (keysItr.hasNext()) {
                val key = keysItr.next()
                val value = json.get(key)
                val map = LinkedHashMap<String, String>()
                if (value is JSONObject) {
                    val iterator = value.keys()
                    while (iterator.hasNext()) {
                        val innerKey = iterator.next()
                        map[innerKey] = value.get(innerKey) as String
                    }
                }
                retMap[key] = map
            }
        } catch (e: JSONException) {
        } catch (e: NullPointerException) {
        }
        return retMap
    }
}