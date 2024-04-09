package com.dhxxn17.data.api

import com.dhxxn17.data.model.RequestData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.QueryMap

interface SwapApi {

    @Multipart
    @POST("character-swap")
    suspend fun requestSwap(
        @Part("character_type") characterType: RequestBody,
        @Part face_img: MultipartBody.Part,
        @Part pose_img: MultipartBody.Part
    ): Response<String>

}