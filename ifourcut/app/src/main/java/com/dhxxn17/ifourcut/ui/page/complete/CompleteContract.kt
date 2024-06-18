package com.dhxxn17.ifourcut.ui.page.complete

import android.graphics.Bitmap
import com.dhxxn17.ifourcut.ui.base.BaseUiAction
import com.dhxxn17.ifourcut.ui.base.BaseUiEffect
import com.dhxxn17.ifourcut.ui.base.BaseUiState
import com.dhxxn17.ifourcut.ui.base.CutState

class CompleteContract {
    data class CompleteState(
        val image: CutState<Bitmap?>,
        val originFaceImg: CutState<Bitmap?>,
        val qrImage: CutState<Bitmap?>,
        val showDialog: CutState<Boolean>,
        val isLoading: CutState<Boolean>
    ): BaseUiState

    sealed class Action: BaseUiAction {
        data class RequestQRCode(val img: Bitmap): Action()

        data class SetShowDialog(val isShow: Boolean): Action()

    }

    sealed class Effect: BaseUiEffect {}
}