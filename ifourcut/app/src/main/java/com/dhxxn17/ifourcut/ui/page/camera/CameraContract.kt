package com.dhxxn17.ifourcut.ui.page.camera

import android.graphics.Bitmap
import com.dhxxn17.ifourcut.ui.base.BaseUiAction
import com.dhxxn17.ifourcut.ui.base.BaseUiEffect
import com.dhxxn17.ifourcut.ui.base.BaseUiState
import com.dhxxn17.ifourcut.ui.base.CutState

class CameraContract {
    data class CameraState(
        val image: CutState<Bitmap?>
    ): BaseUiState

    sealed class Action: BaseUiAction {
        data class SaveImage(
            val image: Bitmap?
        ): Action()
    }

    sealed class Effect: BaseUiEffect {
        object GoToCompleteScreen: Effect()
    }
}