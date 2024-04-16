package com.dhxxn17.ifourcut.common

import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Bitmap.toByteArray(): ByteArray {
    val stream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}

fun Bitmap.toBase64String(): String {
    return Base64.encodeToString(this.toByteArray(), Base64.DEFAULT)
}

fun generateFileName(): String {
    val timestamp = SimpleDateFormat("ddHHmm", Locale.getDefault()).format(Date())
    val randomString = (1..6).map { ('a'..'z').random() }.joinToString("")
    return "image_${timestamp}$randomString.png"
}