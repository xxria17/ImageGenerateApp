package com.dhxxn17.ifourcut.ui.page.complete

import com.dhxxn17.ifourcut.ui.base.BaseUiAction
import com.dhxxn17.ifourcut.ui.base.BaseUiState
import com.dhxxn17.ifourcut.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CompleteViewModel @Inject constructor(

): BaseViewModel() {

    val state: CompleteContract.CompleteState
        get() = state()

    init {
        initialData()
        loadData()
    }

    override fun loadData() {

    }

    override fun initialData() {

    }

    override fun handleEvents(action: BaseUiAction) {

    }

    override fun initialState(): BaseUiState {
        return CompleteContract.CompleteState(
            image = mutableCutStateOf("")
        )
    }
}