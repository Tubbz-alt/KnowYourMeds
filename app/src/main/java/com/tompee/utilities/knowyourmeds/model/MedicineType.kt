package com.tompee.utilities.knowyourmeds.model

import android.content.Context
import com.tompee.utilities.knowyourmeds.R

sealed class MedicineType {

    companion object {
        operator fun get(string: String): MedicineType {
            return when (string) {
                Ingredient.tag -> Ingredient
                MinIngredient.tag -> MinIngredient
                Brand.tag -> Brand
                ClinicalDrugComponent.tag -> ClinicalDrugComponent
                BrandedDrugComponent.tag -> BrandedDrugComponent
                BrandedDrugPack.tag -> BrandedDrugPack
                BrandedDoseFormGroup.tag -> BrandedDoseFormGroup
                ClinicalDrugPack.tag -> ClinicalDrugPack
                ClinicalDoseFormGroup.tag -> ClinicalDoseFormGroup
                PreciseIngredient.tag -> PreciseIngredient
                else -> Nil
            }
        }
    }

    abstract val tag: String

    abstract fun name(context: Context): String
}

object Nil : MedicineType() {
    override val tag: String
        get() = ""

    override fun name(context: Context): String = ""
}

object Ingredient : MedicineType() {
    override val tag: String
        get() = "IN"

    override fun name(context: Context): String = context.getString(R.string.type_ingredient)
}

object MinIngredient : MedicineType() {
    override val tag: String
        get() = "MIN"

    override fun name(context: Context): String = context.getString(R.string.type_ingredient)
}

object Brand : MedicineType() {
    override val tag: String
        get() = "BN"

    override fun name(context: Context): String = context.getString(R.string.type_brands)
}

object ClinicalDrugComponent : MedicineType() {
    override val tag: String
        get() = "SCDC"

    override fun name(context: Context): String = context.getString(R.string.type_scdc)
}

object BrandedDrugComponent : MedicineType() {
    override val tag: String
        get() = "SBDC"

    override fun name(context: Context): String = context.getString(R.string.type_sbdc)
}

object BrandedDrugPack : MedicineType() {
    override val tag: String
        get() = "SBD"

    override fun name(context: Context): String = context.getString(R.string.type_sbd)
}

object BrandedDoseFormGroup : MedicineType() {
    override val tag: String
        get() = "SBDG"

    override fun name(context: Context): String = context.getString(R.string.type_sbdg)
}

object ClinicalDrugPack : MedicineType() {
    override val tag: String
        get() = "SCD"

    override fun name(context: Context): String = context.getString(R.string.type_scd)
}

object ClinicalDoseFormGroup : MedicineType() {
    override val tag: String
        get() = "SCDG"

    override fun name(context: Context): String = context.getString(R.string.type_scdg)
}

object PreciseIngredient : MedicineType() {
    override val tag: String
        get() = "PIN"

    override fun name(context: Context): String = context.getString(R.string.type_pin)
}
