package com.dhxxn17.ifourcut.ui.page.loading

import com.dhxxn17.ifourcut.ui.base.BaseUiAction
import com.dhxxn17.ifourcut.ui.base.BaseUiEffect
import com.dhxxn17.ifourcut.ui.base.BaseUiState
import com.dhxxn17.ifourcut.ui.base.CutState

class LoadingContract {

    data class LoadingState(
        val image: CutState<String>,
        val isCompleted: CutState<Boolean>,
        val isLoading: CutState<Boolean>
    ): BaseUiState

    sealed class Action: BaseUiAction {
        object JobCancel: Action()
        object RequestSwap: Action()

    }

    sealed class Effect: BaseUiEffect {
        object RequestFail: Effect()
    }
}