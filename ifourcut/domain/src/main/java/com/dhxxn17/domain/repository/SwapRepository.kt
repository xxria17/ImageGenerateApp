package com.dhxxn17.domain.repository

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.dhxxn17.domain.model.ResultData

interface SwapRepository {

    suspend fun requestSwap(
        characterType: String,
        faceImage: Bitmap,
        poseImage: Drawable
    ): ResultData<ByteArray?>

}