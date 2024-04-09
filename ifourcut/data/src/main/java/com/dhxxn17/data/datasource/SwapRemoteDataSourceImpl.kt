package com.dhxxn17.data.datasource

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import com.dhxxn17.data.api.SwapApi
import com.dhxxn17.data.model.RequestData
import com.dhxxn17.data.network.apiCall
import com.dhxxn17.data.repository.QueryConstant
import com.dhxxn17.data.response.SwapImageDto
import com.dhxxn17.domain.model.ResultData
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class SwapRemoteDataSourceImpl @Inject constructor(
    private val swapApi: SwapApi
) : SwapRemoteDataSource {

    override suspend fun requestSwap(
        characterType: String,
        beforeImage: Bitmap,
        refImage: Drawable
    ): ResultData<SwapImageDto> {

        val beforeImagePart = bitmapToMultipartBodyPart(beforeImage, "face_img.jpg","face_img")
        val refImagePart = drawableToMultipartBodyPart(refImage, "pose_img.jpg", "pose_img")

        val type = characterType.toRequestBody("multipart/form-data".toMediaTypeOrNull())

        return apiCall {
            swapApi.requestSwap(
                characterType = type,
                face_img = beforeImagePart,
                pose_img = refImagePart
            )
        }
    }

    private fun bitmapToMultipartBodyPart(bitmap: Bitmap, filename: String, name: String): MultipartBody.Part {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        val requestBody = outputStream.toByteArray().toRequestBody("image/jpeg".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(name, filename, requestBody)
    }

    private fun drawableToMultipartBodyPart(drawable: Drawable, filename: String, name: String): MultipartBody.Part {
        val bitmap = (drawable as BitmapDrawable).bitmap
        return bitmapToMultipartBodyPart(bitmap,name, filename)
    }
}