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
        val id: String,

        @ColumnInfo(name = "name", collate = ColumnInfo.NOCASE)
        val name: String,

        @ColumnInfo(name = "url")
        val url: String,

        @ColumnInfo(name = "prescribable")
        val isPrescribable: Boolean,

        @ColumnInfo(name = "type")
        val type: MedicineType,

        @ColumnInfo(name = "ingredients")
        val ingredientList: List<String>,

        @ColumnInfo(name = "favorite")
        val isFavorite: Boolean
) {
    fun convertToMedicine(): Medicine = Medicine(id, name, url, isPrescribable, type, ingredientList, isFavorite)
}