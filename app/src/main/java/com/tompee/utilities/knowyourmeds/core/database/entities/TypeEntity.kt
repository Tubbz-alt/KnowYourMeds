package com.tompee.utilities.knowyourmeds.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.tompee.utilities.knowyourmeds.model.Medicine
import com.tompee.utilities.knowyourmeds.model.MedicineType
import com.tompee.utilities.knowyourmeds.model.Nil

@Entity(tableName = "medicine_type",
        foreignKeys = [ForeignKey(entity = MedicineEntity::class,
                parentColumns = ["id"], childColumns = ["medId"], onDelete = ForeignKey.CASCADE)],
        indices = [Index(value = ["type", "medId"], unique = true)])
data class TypeEntity(
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0,

        @ColumnInfo(name = "medId")
        var medId: String = "",

        @ColumnInfo(name = "type")
        var type: MedicineType = Nil,

        @ColumnInfo(name = "list")
        var list: List<Medicine> = listOf()
)