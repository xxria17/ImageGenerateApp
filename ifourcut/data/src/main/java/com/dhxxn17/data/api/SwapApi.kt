package com.dhxxn17.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.QueryMap

interface SwapApi {

    @POST("character-swap")
    suspend fun requestSwap(
        @QueryMap map : Map<String, String>
    ): Response<String>

}