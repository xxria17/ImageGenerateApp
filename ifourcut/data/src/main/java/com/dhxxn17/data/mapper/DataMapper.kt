package com.dhxxn17.data.mapper

import com.dhxxn17.data.model.SelectData
import com.dhxxn17.domain.model.SelectedData

fun SelectData.asDomain() =
    SelectedData(
        characterType = this.characterType,
        characterImage = this.characterImage,
        myImage = this.myImage,
        completeImage = this.completeImage
    )

fun SelectedData.asData() =
    SelectData(
        characterType = this.characterType,
        characterImage = this.characterImage,
        myImage = this.myImage,
        completeImage = this.completeImage
    )