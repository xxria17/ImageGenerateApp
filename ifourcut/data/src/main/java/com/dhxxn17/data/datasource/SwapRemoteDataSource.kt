package com.dhxxn17.data.datasource

import com.dhxxn17.data.response.SwapImageDto
import com.dhxxn17.domain.model.ResultData

interface SwapRemoteDataSource {

    suspend fun requestSwap(
        beforeImage: String,
        refImage: String
    ): ResultData<SwapImageDto>
}