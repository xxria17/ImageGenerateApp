package com.dhxxn17.domain.repository

import com.dhxxn17.domain.model.ResultData
import com.dhxxn17.domain.model.SwapImage

interface SwapRepository {

    suspend fun requestSwap(
        beforeImage: String,
        refImage: String
    ): ResultData<SwapImage>
}