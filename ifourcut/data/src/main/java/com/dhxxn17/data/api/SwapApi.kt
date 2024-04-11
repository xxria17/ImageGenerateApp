package com.dhxxn17.data.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface SwapApi {

    @Multipart
    @POST("character-swap")
    suspend fun requestSwap(
        @Part("character_type") characterType: RequestBody,
        @Part face_img: MultipartBody.Part,
        @Part pose_img: MultipartBody.Part
    ): Response<ResponseBody>

}