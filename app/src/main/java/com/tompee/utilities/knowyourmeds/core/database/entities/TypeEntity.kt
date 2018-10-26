package com.tompee.utilities.knowyourmeds.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.tompee.utilities.knowyourmeds.model.Medicine
import com.tompee.utilities.knowyourmeds.model.MedicineType
import com.tompee.utilities.knowyourmeds.model.Nil

@Entity(tableName = "medicine_type",
        primaryKeys = ["medId", "type", "typeId"],
        foreignKeys = [ForeignKey(entity = MedicineEntity::class,
                parentColumns = ["id"], childColumns = ["medId"], onDelete = ForeignKey.CASCADE)],
        indices = [Index(name = "typeEntityIndex", value = ["medId", "type", "typeId"], unique = true)])
data class TypeEntity(
        @ColumnInfo(name = "medId")
        var medId: String = "",

        @ColumnInfo(name = "type")
        var type: MedicineType = Nil,

        @ColumnInfo(name = "typeId")
        var typeId: String = "",

        @ColumnInfo(name = "name")
        var name: String = "",

        @ColumnInfo(name = "prescribable")
        var isPrescribable: Boolean = false
) {
    fun convertToMedicine(): Medicine {
        return Medicine(id = typeId, name = name, isPrescribable = isPrescribable, type = type)
    }
}