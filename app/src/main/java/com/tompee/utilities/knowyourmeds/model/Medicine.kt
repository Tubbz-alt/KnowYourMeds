package com.tompee.utilities.knowyourmeds.model

import java.util.*

data class Medicine2(
        var normId: String = "",
        var name: String = "",
        var isPrescribable: Boolean = false,
        var tty: Type? = null,
        var date: Date? = null,
        var url: String = "",
        var sources: List<String?> = listOf(),
        var marketDrugList: MutableList<MarketDrug> = mutableListOf(),
        var brands: List<String> = listOf(),
        var ingredients: List<String> = listOf(),
        var scdc: List<String> = listOf(),
        var sbdc: List<String> = listOf(),
        var sbdg: List<String> = listOf(),
        var scd: List<String> = listOf(),
        var scdg: List<String> = listOf(),
        var sbd: List<String> = listOf(),
        var interactions: Map<String, Map<String, String>> = mapOf()
)