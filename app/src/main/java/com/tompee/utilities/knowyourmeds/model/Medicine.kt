package com.tompee.utilities.knowyourmeds.model

import android.graphics.drawable.Drawable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Medicine(
        @Expose
        @SerializedName("id")
        var id: String = "",

        @Expose
        @SerializedName("name")
        var name: String = "",

        var url: String = "",

        @Expose
        @SerializedName("prescribable")
        var isPrescribable: Boolean = false,

        var type: MedicineType = Ingredient,

        var ingredientList: List<String> = listOf(),

        var drawable: Drawable? = null
)