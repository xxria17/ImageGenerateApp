package com.dhxxn17.domain.usecase

import com.dhxxn17.domain.model.ResultData
import com.dhxxn17.domain.model.SwapImage
import com.dhxxn17.domain.repository.SwapRepository
import javax.inject.Inject

class SwapUseCase @Inject constructor(
    private val swapRepository: SwapRepository
) {

    suspend fun requestSwap(
        beforeImage: String,
        refImage: String
    ): ResultData<SwapImage> {
        return swapRepository.requestSwap(beforeImage, refImage)
    }
}