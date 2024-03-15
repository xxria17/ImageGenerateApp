package com.dhxxn17.ifourcut.ui.page.intro

import com.dhxxn17.ifourcut.ui.base.BaseUiAction
import com.dhxxn17.ifourcut.ui.base.BaseUiState
import com.dhxxn17.ifourcut.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class IntroViewModel @Inject constructor(): BaseViewModel() {

    val state: IntroContract.IntroState
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
        return IntroContract.IntroState(
            image = mutableCutStateListOf(emptyList())
        )
    }
}