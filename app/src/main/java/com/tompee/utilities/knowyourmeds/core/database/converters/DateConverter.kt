package com.tompee.utilities.knowyourmeds.core.database.converters

import android.arch.persistence.room.TypeConverter
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateConverter {
    companion object {
        private const val DATE_FORMAT = "yyyyMMddHHmmss"
    }

    @TypeConverter
    fun toString(date: Date): String? = convertToDateString(date)

    @TypeConverter
    fun toDate(string: String): Date? = convertFromDateString(string)

    private fun convertToDateString(date: Date): String {
        val format1 = SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH)
        return format1.format(date)
    }

    private fun convertFromDateString(string: String): Date? {
        val format = SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH)
        return try {
            return format.parse(string)
        } catch (e: ParseException) {
            null
        }
    }
}