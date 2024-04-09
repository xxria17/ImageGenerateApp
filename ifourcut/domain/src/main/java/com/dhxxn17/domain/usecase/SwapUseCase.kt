package com.dhxxn17.domain.usecase

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.dhxxn17.domain.model.ResultData
import com.dhxxn17.domain.model.SwapImage
import com.dhxxn17.domain.repository.SwapRepository
import javax.inject.Inject

class SwapUseCase @Inject constructor(
    private val swapRepository: SwapRepository
) {

    suspend fun requestSwap(
        characterType: String,
        beforeImage: Bitmap,
        refImage: Drawable
    ): ResultData<SwapImage> {
        return swapRepository.requestSwap(characterType, beforeImage, refImage)
    }
}