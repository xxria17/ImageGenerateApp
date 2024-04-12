package com.dhxxn17.ifourcut.ui.page.loading

import com.dhxxn17.ifourcut.ui.base.BaseUiAction
import com.dhxxn17.ifourcut.ui.base.BaseUiEffect
import com.dhxxn17.ifourcut.ui.base.BaseUiState
import com.dhxxn17.ifourcut.ui.base.CutState

class LoadingContract {

    data class LoadingState(
        val image: CutState<String>
    ): BaseUiState

    sealed class Action: BaseUiAction {
        object JobCancel: Action()
    }

    sealed class Effect: BaseUiEffect {
        object GoToComplete: Effect()
        object RequestFail: Effect()
    }
}