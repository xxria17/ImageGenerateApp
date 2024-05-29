package com.dhxxn17.ifourcut.common

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import com.dhxxn17.ifourcut.ui.base.findActivity
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.OutputStream

fun convertDrawableToBase64String(context: Context, drawableId: Int): String {
    val bitmap = BitmapFactory.decodeResource(context.resources, drawableId)

    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()

    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}

fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
//    return MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        BitmapFactory.decodeStream(inputStream)
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
        null
    }
}

fun bitmapToString(bitmap: Bitmap): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}

fun stringToBitmap(encodedString: String): Bitmap {
    val decodedBytes = Base64.decode(encodedString, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
}

fun saveBitmapImage(bitmap: Bitmap, context: Context) {
    val timestamp = System.currentTimeMillis()
    val contentResolver = context.findActivity().contentResolver
    val values = ContentValues()
    values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
    values.put(MediaStore.Images.Media.DATE_ADDED, timestamp)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        values.put(MediaStore.Images.Media.DATE_TAKEN, timestamp)
        values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/" + generateFileName())
        values.put(MediaStore.Images.Media.IS_PENDING, true)
        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        if (uri != null) {
            try {
                val outputStream = contentResolver.openOutputStream(uri)
                if (outputStream != null) {
                    try {
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                        outputStream.close()
                    } catch (e: Exception) {
                        Log.e("ImageUtils", "saveBitmapImage: ", e)
                    }
                }
                values.put(MediaStore.Images.Media.IS_PENDING, false)
                contentResolver.update(uri, values, null, null)

            } catch (e: Exception) {
                Log.e("ImageUtils", "saveBitmapImage: ", e)
            }
        }
    } else {
        val imageFileFolder = File(
            Environment.getExternalStorageDirectory().toString() + '/' + generateFileName()
        )
        if (!imageFileFolder.exists()) {
            imageFileFolder.mkdirs()
        }
        val mImageName = "$timestamp.png"
        val imageFile = File(imageFileFolder, mImageName)
        try {
            val outputStream: OutputStream = FileOutputStream(imageFile)
            try {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.close()
            } catch (e: Exception) {
                Log.e("ImageUtils", "saveBitmapImage: ", e)
            }
            values.put(MediaStore.Images.Media.DATA, imageFile.absolutePath)
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        } catch (e: Exception) {
            Log.e("ImageUtils", "saveBitmapImage: ", e)
        }
    }
}