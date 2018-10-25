package com.tompee.utilities.knowyourmeds.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tompee.utilities.knowyourmeds.model.Ingredient
import com.tompee.utilities.knowyourmeds.model.Medicine
import com.tompee.utilities.knowyourmeds.model.MedicineType

@Entity(tableName = "medicine")
data class MedicineEntity(
        @PrimaryKey
        @ColumnInfo(name = "id")
        var id: String = "",

        @ColumnInfo(name = "name", collate = ColumnInfo.NOCASE)
        var name: String = "",

        @ColumnInfo(name = "url")
        var url: String = "",

        @ColumnInfo(name = "prescribable")
        var isPrescribable: Boolean = false,

        @ColumnInfo(name = "type")
        var type: MedicineType = Ingredient,

        @ColumnInfo(name = "ingredients")
        var ingredientList: List<String> = listOf()
) {
    fun convertToMedicine(): Medicine = Medicine(id, name, url, isPrescribable, type, ingredientList)
}