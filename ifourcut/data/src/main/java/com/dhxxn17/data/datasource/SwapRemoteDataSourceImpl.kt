package com.dhxxn17.data.datasource

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.dhxxn17.data.api.SwapApi
import com.dhxxn17.data.mapper.toByteArray
import com.dhxxn17.data.network.apiCall
import com.dhxxn17.domain.model.ResultData
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
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

        val typePart = characterType.toRequestBody("text/plain".toMediaTypeOrNull())

        val faceRequestBody = faceByteArray.toRequestBody("image/png".toMediaTypeOrNull())
        val facePart =
            MultipartBody.Part.createFormData("face_img", "face_image.png", faceRequestBody)

        val poseRequestBody = poseByteArray.toRequestBody("image/png".toMediaTypeOrNull())
        val posePart =
            MultipartBody.Part.createFormData("pose_img", "pose_image.png", poseRequestBody)

        return apiCall {
            swapApi.requestSwap(
                characterType = typePart,
                face_img = facePart,
                pose_img = posePart
            )
        }
    }
}