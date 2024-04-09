package com.dhxxn17.data.repository

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.dhxxn17.data.datasource.SwapRemoteDataSource
import com.dhxxn17.data.mapper.asDomain
import com.dhxxn17.domain.model.ResultData
import com.dhxxn17.domain.model.SwapImage
import com.dhxxn17.domain.repository.SwapRepository
import javax.inject.Inject

class SwapRepositoryImpl @Inject constructor(
    private val swapRemoteDataSource: SwapRemoteDataSource
): SwapRepository {

    override suspend fun requestSwap(
        characterType: String,
        beforeImage: Bitmap,
        refImage: Drawable
    ): ResultData<SwapImage> {
        val result = swapRemoteDataSource.requestSwap(
            characterType, beforeImage, refImage
        )
        return if (result is ResultData.Success) {
            ResultData.Success(
                successData = result.data?.asDomain() ?: SwapImage()
            )
        } else {
            ResultData.Error(errorMessage = result.message)
        }
    }
}