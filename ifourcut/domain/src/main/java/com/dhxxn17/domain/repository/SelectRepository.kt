package com.dhxxn17.domain.repository

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.dhxxn17.domain.model.SelectedData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface SelectRepository {
    val selectDataFlow: SharedFlow<SelectedData>
    suspend fun saveCharacterData(type: String, image: Drawable)
    suspend fun saveUserImage(myImage: Bitmap)
    suspend fun getAllData(): Flow<SelectedData>
    suspend fun saveCompleteImage(image: ByteArray?)
}