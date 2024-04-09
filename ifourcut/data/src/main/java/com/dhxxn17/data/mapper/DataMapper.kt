package com.dhxxn17.data.mapper

import com.dhxxn17.data.response.SwapImageDto
import com.dhxxn17.domain.model.SwapImage

fun SwapImageDto.asDomain() =
    SwapImage(
        resultImage = this.resultImage,
    )
