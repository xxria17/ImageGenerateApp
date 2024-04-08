package com.dhxxn17.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SwapImage(
    val id: String = "",
    val resultImage: String = "",
    val createdTime: String = ""
): Parcelable
