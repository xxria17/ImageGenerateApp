package com.dhxxn17.ifourcut.ui.page.start.princess

import com.dhxxn17.ifourcut.ui.base.BaseUiAction
import com.dhxxn17.ifourcut.ui.base.BaseUiEffect
import com.dhxxn17.ifourcut.ui.base.BaseUiState
import com.dhxxn17.ifourcut.ui.base.CutStateList

class PrincessContract {
    data class PrincessState(
        val image: CutStateList<String>
    ): BaseUiState

    sealed class Action: BaseUiAction

    sealed class Effect: BaseUiEffect
}