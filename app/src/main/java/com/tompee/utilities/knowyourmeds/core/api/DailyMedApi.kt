package com.tompee.utilities.knowyourmeds.core.api

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface DailyMedApi {
    @GET("spls.json")
    fun getSpl(@Query("rxcui") id: String,
               @Query("page") page: Int): Single<SplModel>
}