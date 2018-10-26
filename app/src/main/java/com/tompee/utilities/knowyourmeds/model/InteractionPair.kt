package com.tompee.utilities.knowyourmeds.model

import android.graphics.drawable.Drawable

data class InteractionPair(
        val source: String,
        val sourceUrl: String,
        val partner: String,
        val partnerUrl: String,
        val interaction: String,
        var drawable: Drawable? = null
)