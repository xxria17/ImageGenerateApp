package com.dhxxn17.ifourcut.ui.page.camera

import android.graphics.Bitmap
import com.dhxxn17.ifourcut.ui.base.BaseUiAction
import com.dhxxn17.ifourcut.ui.base.BaseUiEffect
import com.dhxxn17.ifourcut.ui.base.BaseUiState
import com.dhxxn17.ifourcut.ui.base.CutState

class CameraCompleteContract {
    data class CameraCompleteState(
        val image: CutState<Bitmap?>
    ): BaseUiState

    sealed class Action: BaseUiAction {
    }

    sealed class Effect: BaseUiEffect {
    }
}