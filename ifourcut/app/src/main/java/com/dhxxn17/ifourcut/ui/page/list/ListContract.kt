package com.dhxxn17.ifourcut.ui.page.list

import com.dhxxn17.ifourcut.model.ListData
import com.dhxxn17.ifourcut.ui.base.BaseUiAction
import com.dhxxn17.ifourcut.ui.base.BaseUiEffect
import com.dhxxn17.ifourcut.ui.base.BaseUiState
import com.dhxxn17.ifourcut.ui.base.CutStateList

class ListContract {
    data class ListState(
        val data: CutStateList<ListData>
    ): BaseUiState

    sealed class Action: BaseUiAction

    sealed class Effect: BaseUiEffect
}