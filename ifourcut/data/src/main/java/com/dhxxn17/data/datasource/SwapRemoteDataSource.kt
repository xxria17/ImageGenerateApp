package com.dhxxn17.data.datasource

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.dhxxn17.domain.model.ResultData

interface SwapRemoteDataSource {

    suspend fun requestSwap(
        characterType: String,
        faceImage: Bitmap,
        poseImage: Drawable
    ): ResultData<ByteArray?>

    suspend fun requestQR(
        resultImage: Bitmap
    ): ResultData<ByteArray?>

}