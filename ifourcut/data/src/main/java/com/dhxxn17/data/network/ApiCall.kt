package com.dhxxn17.data.network

import android.util.Log
import com.dhxxn17.domain.model.ResultData
import okhttp3.ResponseBody
import retrofit2.Response


suspend inline fun apiCall(
    call: suspend () -> Response<ResponseBody>,
): ResultData<ByteArray?> {
    return try {

        val response = call.invoke()
//        val response = call.invoke()
        if (response.isSuccessful) {
            ResultData.Success(response.body()?.bytes())
        } else {
            ResultData.Error(errorMessage = response.errorBody()?.toString() ?: "response error")
        }

    } catch (e: Exception) {
        Log.e("API CALL","${e.cause} \n${e.message}")
        ResultData.Error(errorMessage = e.message)
    }
}