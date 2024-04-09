package com.dhxxn17.domain.model

import android.graphics.Bitmap
import android.graphics.drawable.Drawable

data class SelectedData(
    val characterType: String = "",
    val characterImage: Drawable?,
    val myImage: Bitmap?
)
