package com.tompee.utilities.knowyourmeds.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.tompee.utilities.knowyourmeds.model.Medicine
import com.tompee.utilities.knowyourmeds.model.MedicineType

@Entity(tableName = "medicine_type",
        primaryKeys = ["medId", "type", "typeId"],
        foreignKeys = [ForeignKey(entity = MedicineEntity::class,
                parentColumns = ["id"], childColumns = ["medId"], onDelete = ForeignKey.CASCADE)],
        indices = [Index(name = "typeEntityIndex", value = ["medId", "type", "typeId"], unique = true)])
data class TypeEntity(
        @ColumnInfo(name = "medId")
        val medId: String,

        @ColumnInfo(name = "type")
        val type: MedicineType,

        @ColumnInfo(name = "typeId")
        val typeId: String,

        @ColumnInfo(name = "name")
        val name: String,

        @ColumnInfo(name = "prescribable")
        val isPrescribable: Boolean
) {
    fun convertToMedicine(): Medicine {
        return Medicine(id = typeId, name = name, isPrescribable = isPrescribable, type = type)
    }
}