package com.tompee.utilities.knowyourmeds.core.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.tompee.utilities.knowyourmeds.core.database.converters.DateConverter
import com.tompee.utilities.knowyourmeds.core.database.converters.NestedMapConverter
import com.tompee.utilities.knowyourmeds.core.database.converters.StringListConverter
import com.tompee.utilities.knowyourmeds.core.database.entities.Main

@Database(entities = [Main::class], version = 5)
@TypeConverters(StringListConverter::class, DateConverter::class, NestedMapConverter::class)
abstract class Database : RoomDatabase() {
    abstract fun mainDAO(): MainDAO
}