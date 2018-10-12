package com.tompee.utilities.knowyourmeds.core.api

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MedApi {
    @GET("rxcui.json")
    fun getRxNormId(@Query("name") name: String): Single<RxNormModel>

    @GET("rxcui/{propId}/property.json")
    fun getPropertyName(@Path("propId") propId: String,
                        @Query("propName") propName: String = "RxNorm " +
                                "Name"): Single<PropertyGroup>

    @GET("rxcui/{propId}/property.json")
    fun getPrescribable(@Path("propId") propId: String,
                        @Query("propName") propName: String = "PRESCRIBABLE"): Single<PropertyGroup>

    @GET("rxcui/{propId}/property.json")
    fun getTty(@Path("propId") propId: String,
               @Query("propName") propName: String = "TTY"): Single<PropertyGroup>

    @GET("rxcui/{propId}/property.json")
    fun getSources(@Path("propId") propId: String,
                   @Query("propName") prop: String = "Source"): Single<PropertyGroup>

    @GET("spellingsuggestions.json")
    fun getSuggestions(@Query("name") name: String): Single<SuggestionModel>

    @GET("rxcui/{propId}/allProperties.json")
    fun getAttributes(@Path("propId") propId: String,
                      @Query("prop") prop: String = "ATTRIBUTES"): Single<PropertyGroup>

    @GET("rxcui/{propId}/related.json")
    fun getTtyValues(@Path("propId") propId: String,
                     @Query("tty", encoded = true) tty: String = "BN+IN+SCDC+SBDC+SBDG+SCD+SCDG+SBD"): Single<TtyGroup>

    @GET("interaction/interaction.json")
    fun getInteraction(@Query("rxcui") id: String): Single<InteractionModel>
}