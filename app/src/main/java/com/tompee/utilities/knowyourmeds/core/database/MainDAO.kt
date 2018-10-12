package com.tompee.utilities.knowyourmeds.core.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import com.tompee.utilities.knowyourmeds.core.database.entities.Main

@Dao
interface MainDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(main: Main)
}