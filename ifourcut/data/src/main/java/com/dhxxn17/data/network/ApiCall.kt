package com.dhxxn17.data.network

import android.util.Log
import com.dhxxn17.domain.model.ResultData
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import retrofit2.Response


suspend inline fun <reified T : Any> apiCall(
    call: suspend () -> Response<String>
): ResultData<T> {
    return try {
        val json = Json {
            encodeDefaults = true
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
        val response = call.invoke()
        val responseStr = response.body() ?: ""

        if (response.isSuccessful) {
            ResultData.Success(json.decodeFromString<T>(responseStr))
        } else {
            ResultData.Error(errorMessage = response.errorBody()?.toString() ?: "response error")
        }

    } catch (e: Exception) {
        Log.e("API CALL","${e.cause} \n${e.message}")
        ResultData.Error(errorMessage = e.message)
    }
}