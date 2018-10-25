package com.tompee.utilities.knowyourmeds.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.tompee.utilities.knowyourmeds.model.InteractionPair

@Entity(tableName = "interactions",
        foreignKeys = [ForeignKey(entity = MedicineEntity::class,
                parentColumns = ["id"], childColumns = ["medId"], onDelete = ForeignKey.CASCADE)],
        indices = [Index(value = ["medId"], unique = true)])
data class InteractionEntity(
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0,

        @ColumnInfo(name = "medId")
        var medId: String = "",

        @ColumnInfo(name = "list")
        var list: List<InteractionPair> = listOf()
)