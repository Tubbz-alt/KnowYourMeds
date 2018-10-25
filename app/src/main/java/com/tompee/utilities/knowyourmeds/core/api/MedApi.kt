package com.tompee.utilities.knowyourmeds.core.api

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MedApi {

    @GET("rxcui.json")
    fun getRxNormId(@Query("name") name: String): Single<RxNormModel>

    @GET("rxcui/{propId}/allProperties.json?prop=NAMES")
    fun getName(@Path("propId") propId: String): Single<PropertyGroup>

    @GET("rxcui/{propId}/allProperties.json?prop=ATTRIBUTES")
    fun getAttributes(@Path("propId") propId: String): Single<PropertyGroup>

    @GET("rxcui/{propId}/related.json")
    fun getTtyValues(@Path("propId") propId: String,
                     @Query("tty", encoded = true) tty: String = "BN+IN+SCDC+SBDC+SBDG+SCD+SCDG+SBD"): Single<TtyGroup>

    @GET("rxcui/{propId}/property.json")
    fun getSources(@Path("propId") propId: String,
                   @Query("propName") prop: String = "Source"): Single<PropertyGroup>

    @GET("spellingsuggestions.json")
    fun getSuggestions(@Query("name") name: String): Single<SuggestionModel>

    @GET("interaction/interaction.json")
    fun getInteraction(@Query("rxcui") id: String): Single<InteractionModel>
}