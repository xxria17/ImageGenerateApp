package com.dhxxn17.data.datasource

import com.dhxxn17.data.api.SwapApi
import com.dhxxn17.data.network.apiCall
import com.dhxxn17.data.repository.QueryConstant
import com.dhxxn17.data.response.SwapImageDto
import com.dhxxn17.domain.model.ResultData
import javax.inject.Inject

class SwapRemoteDataSourceImpl @Inject constructor(
    private val swapApi: SwapApi
): SwapRemoteDataSource {

    override suspend fun requestSwap(
        beforeImage: String,
        refImage: String
    ): ResultData<SwapImageDto> {
        val map = hashMapOf<String, String>().apply {
            put(QueryConstant.BEFORE_IMAGE, beforeImage)
            put(QueryConstant.REF_IMAGE, refImage)
        }

        return apiCall {
            swapApi.requestSwap(map)
        }
    }
}