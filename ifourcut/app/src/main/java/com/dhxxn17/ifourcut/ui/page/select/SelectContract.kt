package com.dhxxn17.ifourcut.ui.page.select

import android.graphics.drawable.Drawable
import com.dhxxn17.ifourcut.ui.base.BaseUiAction
import com.dhxxn17.ifourcut.ui.base.BaseUiEffect
import com.dhxxn17.ifourcut.ui.base.BaseUiState
import com.dhxxn17.ifourcut.ui.base.CutStateList

class SelectContract {
    data class SelectState(
        val imageList: CutStateList<Int>,
        val nameList: CutStateList<String>
    ): BaseUiState

    sealed class Action: BaseUiAction {
        data class SelectCharacter(
            val type: String,
            val image: Drawable
        ): Action()

        data class SetType(
            val type: String
        ): Action()
    }

    sealed class Effect: BaseUiEffect {
        object GoToUploadScreen: Effect()
    }
}