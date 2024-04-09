package com.dhxxn17.domain.repository

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.dhxxn17.domain.model.ResultData
import com.dhxxn17.domain.model.SwapImage

interface SwapRepository {

    suspend fun requestSwap(
        characterType: String,
        beforeImage: Bitmap,
        refImage: Drawable
    ): ResultData<SwapImage>
}