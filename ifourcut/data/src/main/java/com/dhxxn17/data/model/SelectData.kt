package com.dhxxn17.data.model

import android.graphics.Bitmap
import android.graphics.drawable.Drawable

data class SelectData(
    val characterType: String = "",
    val characterImage: Drawable?,
    val myImage: Bitmap?,
    val completeImage: ByteArray?
)
