package com.tompee.utilities.knowyourmeds.model

import android.graphics.drawable.Drawable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class InteractionPair(

        @Expose
        @SerializedName("source")
        val source: String,

        @Expose
        @SerializedName("sourceUrl")
        val sourceUrl: String,

        @Expose
        @SerializedName("partner")
        val partner: String,

        @Expose
        @SerializedName("partnerUrl")
        val partnerUrl: String,

        @Expose
        @SerializedName("interaction")
        val interaction: String,

        var drawable: Drawable? = null
)