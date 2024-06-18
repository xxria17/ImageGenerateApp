package com.dhxxn17.data.datasource

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.dhxxn17.data.api.QRApi
import com.dhxxn17.data.api.SwapApi
import com.dhxxn17.data.mapper.toByteArray
import com.dhxxn17.data.network.apiCall
import com.dhxxn17.data.network.qrApiCall
import com.dhxxn17.domain.model.ResultData
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

class SwapRemoteDataSourceImpl @Inject constructor(
    private val swapApi: SwapApi,
    private val qrApi: QRApi
) : SwapRemoteDataSource {

    override suspend fun requestSwap(
        characterType: String,
        faceImage: Bitmap,
        poseImage: Drawable
    ): ResultData<ByteArray?> {
        val resizeBitmap = resizeBitmap(faceImage)
        val faceByteArray = resizeBitmap.toByteArray()
        val poseByteArray = poseImage.toByteArray()

        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val faceFileName = "face_img$timeStamp.png"
        val poseFileName = "pose_img$timeStamp.png"

        val typePart = characterType.toRequestBody("text/plain".toMediaTypeOrNull())

        val faceRequestBody = faceByteArray.toRequestBody("image/png".toMediaTypeOrNull())
        val facePart =
            MultipartBody.Part.createFormData("face_img", faceFileName, faceRequestBody)

        val poseRequestBody = poseByteArray.toRequestBody("image/png".toMediaTypeOrNull())
        val posePart =
            MultipartBody.Part.createFormData("pose_img", poseFileName, poseRequestBody)

        return apiCall {
            swapApi.requestSwap(
                characterType = typePart,
                face_img = facePart,
                pose_img = posePart
            )
        }
    }

    override suspend fun requestQR(
        resultImage: Bitmap
    ): ResultData<ByteArray?> {
        val resultByteArray = resultImage.toByteArray()
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val resultFileName = "result_img$timeStamp.png"
        val resultRequestBody = resultByteArray.toRequestBody("image/png".toMediaTypeOrNull())
        val resultPart = MultipartBody.Part.createFormData("file", resultFileName, resultRequestBody)

        return apiCall {
            qrApi.requestQR(
                result_img = resultPart
            )
        }
    }

    private fun resizeBitmap(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val maxWidth = 700
        val maxHeight = 860

        val widthRatio = maxWidth.toFloat() / width
        val heightRatio = maxHeight.toFloat() / height
        val scaleFactor = Math.min(widthRatio, heightRatio)

        val scaledWidth = (width * scaleFactor).toInt()
        val scaledHeight = (height * scaleFactor).toInt()

        return Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true)
    }
}