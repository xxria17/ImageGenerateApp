package com.dhxxn17.ifourcut.ui.page.camera

import android.graphics.Bitmap
import com.dhxxn17.ifourcut.ui.base.BaseUiAction
import com.dhxxn17.ifourcut.ui.base.BaseUiEffect
import com.dhxxn17.ifourcut.ui.base.BaseUiState
import com.dhxxn17.ifourcut.ui.base.CutState
import com.dhxxn17.ifourcut.ui.page.loading.LoadingContract

class CameraCompleteContract {
    data class CameraCompleteState(
        val image: CutState<Bitmap?>,
        val isCompleted: CutState<Boolean>,
        val isRequest: CutState<Boolean>
    ): BaseUiState

    sealed class Action: BaseUiAction {
        object JobCancel: Action()
        object RequestSwap: Action()
    }

    sealed class Effect: BaseUiEffect {
        object RequestFail: Effect()
    }
}