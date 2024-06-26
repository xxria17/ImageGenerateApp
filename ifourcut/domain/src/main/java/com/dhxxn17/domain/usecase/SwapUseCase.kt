package com.dhxxn17.domain.usecase

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.dhxxn17.domain.model.ResultData
import com.dhxxn17.domain.repository.SwapRepository
import javax.inject.Inject

class SwapUseCase @Inject constructor(
    private val swapRepository: SwapRepository
) {

    suspend fun requestSwap(
        characterType: String,
        faceImage: Bitmap,
        poseImage: Drawable
    ): ResultData<ByteArray?> {
        return swapRepository.requestSwap(characterType, faceImage, poseImage)
    }

}