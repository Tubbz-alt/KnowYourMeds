package com.tompee.utilities.knowyourmeds.model

import android.content.Context
import com.tompee.utilities.knowyourmeds.R

sealed class Type {
    companion object {
        fun getType(string: String): Type {
            return when (string) {
                Property.tag -> Property
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
                Page.tag -> Page
                Interaction.tag -> Interaction
                Sources.tag -> Sources
                else -> Ingredient
            }
        }
    }

    abstract val tag: String

    abstract fun name(context: Context): String
}

object Property : Type() {
    override val tag: String
        get() = "PROP"

    override fun name(context: Context): String = context.getString(R.string.tab_properties)
}

object Ingredient : Type() {
    override val tag: String
        get() = "IN"

    override fun name(context: Context): String = context.getString(R.string.property_ingredient)
}

object MinIngredient : Type() {
    override val tag: String
        get() = "MIN"

    override fun name(context: Context): String = context.getString(R.string.property_ingredient)
}

object Brand : Type() {
    override val tag: String
        get() = "BN"

    override fun name(context: Context): String = context.getString(R.string.property_brands)
}

object ClinicalDrugComponent : Type() {
    override val tag: String
        get() = "SCDC"

    override fun name(context: Context): String = context.getString(R.string.tab_scdc)
}

object BrandedDrugComponent : Type() {
    override val tag: String
        get() = "SBDC"

    override fun name(context: Context): String = context.getString(R.string.tab_sbdc)
}

object BrandedDrugPack : Type() {
    override val tag: String
        get() = "SBD"

    override fun name(context: Context): String = context.getString(R.string.tab_sbd)
}

object BrandedDoseFormGroup : Type() {
    override val tag: String
        get() = "SBDG"

    override fun name(context: Context): String = context.getString(R.string.tab_sbdg)
}

object ClinicalDrugPack : Type() {
    override val tag: String
        get() = "SCD"

    override fun name(context: Context): String = context.getString(R.string.tab_scd)
}

object ClinicalDoseFormGroup : Type() {
    override val tag: String
        get() = "SCDG"

    override fun name(context: Context): String = context.getString(R.string.tab_scdg)
}

object PreciseIngredient : Type() {
    override val tag: String
        get() = "PIN"

    override fun name(context: Context): String = context.getString(R.string.tab_pin)
}

object Page : Type() {
    override val tag: String
        get() = "PAGE"

    override fun name(context: Context): String = context.getString(R.string.tab_info)
}

object Interaction : Type() {
    override val tag: String
        get() = "INTERACTION"

    override fun name(context: Context): String = context.getString(R.string.tab_interaction)
}

object Sources : Type() {
    override val tag: String
        get() = "SOURCES"

    override fun name(context: Context): String = context.getString(R.string.tab_sources)
}