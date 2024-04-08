package com.dhxxn17.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SwapImageDto(
    @SerialName("id")
    val id: String = "",

    @SerialName("result")
    val resultImage: String = "",

    @SerialName("createdAt")
    val createdAt: String = ""
)