package com.dhxxn17.ifourcut.ui.page.select

import com.dhxxn17.ifourcut.ui.base.BaseUiAction
import com.dhxxn17.ifourcut.ui.base.BaseUiEffect
import com.dhxxn17.ifourcut.ui.base.BaseUiState
import com.dhxxn17.ifourcut.ui.base.CutStateList

class SelectContract {
    data class SelectState(
        val imageList: CutStateList<String>,
        val nameList: CutStateList<String>
    ): BaseUiState

    sealed class Action: BaseUiAction {}

    sealed class Effect: BaseUiEffect {}
}