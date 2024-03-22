package com.dhxxn17.ifourcut.common

import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream

fun Bitmap.toByteArray(): ByteArray {
    val stream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}

fun Bitmap.toBase64String(): String {
    return Base64.encodeToString(this.toByteArray(), Base64.DEFAULT)
}