package com.dhxxn17.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface SwapApi {

    @GET("")
    suspend fun requestSwap(
        @QueryMap map : Map<String, String>
    ): Response<String>

}