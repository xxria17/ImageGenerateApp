package com.dhxxn17.domain.usecase

import android.graphics.Bitmap
import com.dhxxn17.domain.model.ResultData
import com.dhxxn17.domain.repository.SwapRepository
import javax.inject.Inject

class RequestQRUseCase @Inject constructor(
    private val swapRepository: SwapRepository
) {

    suspend fun requestQR(
        resultImg: Bitmap
    ): ResultData<ByteArray?> {
        return swapRepository.requestQR(resultImg)
    }
}