package com.dhxxn17.ifourcut.ui.page.upload

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import com.dhxxn17.ifourcut.ui.base.BaseUiAction
import com.dhxxn17.ifourcut.ui.base.BaseUiEffect
import com.dhxxn17.ifourcut.ui.base.BaseUiState
import com.dhxxn17.ifourcut.ui.base.CutState

class UploadContract {
    data class UploadState(
        val galleryImage: CutState<Uri?>,
        val cameraImage: CutState<String>,
        val characterImage: CutState<Drawable?>
    ): BaseUiState

    sealed class Action: BaseUiAction {
        data class SetCameraImage(
            val imageBitmap: String
        ): Action()
        data class SetGalleryImage(
            val imageUri: Uri
        ): Action()

        data class SelectImage(
            val myImage: Bitmap
        ): Action()
    }

    sealed class Effect: BaseUiEffect {
        object GoToLoadingScreen: Effect()
    }
}