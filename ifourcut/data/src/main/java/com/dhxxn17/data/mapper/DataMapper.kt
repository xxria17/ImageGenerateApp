package com.dhxxn17.data.mapper

import com.dhxxn17.data.model.SelectData
import com.dhxxn17.data.response.SwapImageDto
import com.dhxxn17.domain.model.SelectedData
import com.dhxxn17.domain.model.SwapImage

fun SwapImageDto.asDomain() =
    SwapImage(
        resultImage = this.resultImage,
    )

fun SelectData.asDomain() =
    SelectedData(
        characterType = this.characterType,
        characterImage = this.characterImage,
        myImage = this.myImage
    )

fun SelectedData.asData() =
    SelectData(
        characterType = this.characterType,
        characterImage = this.characterImage,
        myImage = this.myImage
    )