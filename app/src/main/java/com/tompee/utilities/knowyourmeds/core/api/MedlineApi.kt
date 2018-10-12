package com.tompee.utilities.knowyourmeds.core.api

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MedlineApi {
    @GET("medlineplus/services/mpconnect_service.cfm")
    fun getMedlineUrl(@Query("mainSearchCriteria.v.c") id: String,
                      @Query("knowledgeResponseType") type: String = "application/json",
                      @Query("mainSearchCriteria.v.cs") vcs: String = "2.16.840.1.113883.6.88"): Single<MedlineUrlModel>
}