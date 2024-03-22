package com.dhxxn17.ifourcut.ui.page.loading

import com.dhxxn17.ifourcut.ui.base.BaseUiAction
import com.dhxxn17.ifourcut.ui.base.BaseUiState
import com.dhxxn17.ifourcut.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoadingViewModel @Inject constructor(): BaseViewModel() {

    val state: LoadingContract.LoadingState
        get() = state()

    init {
        initialData()
        loadData()
    }

    override fun loadData() {
    }

    override fun initialData() {
        state.image.sendState { "" }
    }

    override fun handleEvents(action: BaseUiAction) {
    }

    override fun initialState(): BaseUiState {
        return LoadingContract.LoadingState(
            image = mutableCutStateOf("")
        )
    }

}