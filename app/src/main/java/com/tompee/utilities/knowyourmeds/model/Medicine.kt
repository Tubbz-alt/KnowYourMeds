package com.tompee.utilities.knowyourmeds.model

import android.graphics.drawable.Drawable

data class Medicine(
        var id: String = "",
        var name: String = "",
        var url: String = "",
        var isPrescribable: Boolean = false,
        var type: MedicineType = Ingredient,
        var ingredientList: List<String> = listOf(),
        var isFavorite : Boolean = false,
        var drawable: Drawable? = null
)