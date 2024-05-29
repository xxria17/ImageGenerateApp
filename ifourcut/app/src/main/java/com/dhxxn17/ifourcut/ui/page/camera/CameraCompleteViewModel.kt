package com.dhxxn17.ifourcut.ui.page.camera

import androidx.lifecycle.viewModelScope
import com.dhxxn17.domain.usecase.SendAllSelectDataUseCase
import com.dhxxn17.ifourcut.ui.base.BaseUiAction
import com.dhxxn17.ifourcut.ui.base.BaseUiState
import com.dhxxn17.ifourcut.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraCompleteViewModel @Inject constructor(
    private val useCase: SendAllSelectDataUseCase
) : BaseViewModel() {

    val state: CameraCompleteContract.CameraCompleteState
        get() = state()

    private var job: Job? = null

    init {
        initialData()
        loadData()
    }


    override fun loadData() {
        getUserImage()
    }

    override fun initialData() {
        state.image.sendState { null }
    }

    override fun handleEvents(action: BaseUiAction) {
    }


    override fun initialState(): BaseUiState {
        return CameraCompleteContract.CameraCompleteState(
            image = mutableCutStateOf(null)
        )
    }

    private fun getUserImage() {
        if (job != null && job?.isActive == true) return

        job = viewModelScope.launch {
            val data = useCase.invoke()
            state.image.sendState { data.myImage }
        }
    }

}