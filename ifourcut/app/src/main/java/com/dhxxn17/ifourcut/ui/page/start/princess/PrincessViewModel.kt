package com.dhxxn17.ifourcut.ui.page.start.princess

import com.dhxxn17.ifourcut.ui.base.BaseUiAction
import com.dhxxn17.ifourcut.ui.base.BaseUiState
import com.dhxxn17.ifourcut.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PrincessViewModel @Inject constructor(): BaseViewModel() {

    val state: PrincessContract.PrincessState
        get() = state()

    init {
        initialData()
        loadData()
    }

    override fun loadData() {

    }

    override fun initialData() {
        state.image.sendState { emptyList() }
    }

    override fun handleEvents(action: BaseUiAction) {

    }

    override fun initialState(): BaseUiState {
        return PrincessContract.PrincessState(
            image = mutableCutStateListOf(emptyList())
        )
    }
}