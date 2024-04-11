package com.dhxxn17.data.datasource

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import com.dhxxn17.data.api.SwapApi
import com.dhxxn17.data.network.apiCall
import com.dhxxn17.domain.model.ResultData
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class SwapRemoteDataSourceImpl @Inject constructor(
    private val swapApi: SwapApi
) : SwapRemoteDataSource {

    override suspend fun requestSwap(
        characterType: String,
        faceImage: Bitmap,
        poseImage: Drawable
    ): ResultData<ByteArray?> {

        val faceByteArray = faceImage.toByteArray()
        val poseByteArray = poseImage.toByteArray()

        val typePart = RequestBody.create("text/plain".toMediaTypeOrNull(), characterType)
        val facePart = MultipartBody.Part.createFormData(
            "face_img",
            "face_image.png",
            RequestBody.create("image/png".toMediaTypeOrNull(), faceByteArray)
        )
        val posePart = MultipartBody.Part.createFormData(
            "pose_img",
            "pose_image.png",
            RequestBody.create("image/png".toMediaTypeOrNull(), poseByteArray)
        )

       // val type = characterType.toRequestBody("multipart/form-data".toMediaTypeOrNull())

        return apiCall {
            swapApi.requestSwap(
                characterType = typePart,
                face_img = facePart,
                pose_img = posePart
            )
        }
    }

    fun Bitmap.toByteArray(): ByteArray {
        val stream = ByteArrayOutputStream()
        this.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    fun Drawable.toBitmap(): Bitmap {
        if (this is BitmapDrawable) {
            return this.bitmap
        }

        val bitmap = Bitmap.createBitmap(
            this.intrinsicWidth,
            this.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        this.setBounds(0, 0, canvas.width, canvas.height)
        this.draw(canvas)
        return bitmap
    }

    fun Drawable.toByteArray(): ByteArray {
        return this.toBitmap().toByteArray()
    }
}