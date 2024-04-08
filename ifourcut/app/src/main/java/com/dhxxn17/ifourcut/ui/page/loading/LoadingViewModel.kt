package com.dhxxn17.ifourcut.ui.page.loading

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.dhxxn17.domain.model.ResultData
import com.dhxxn17.domain.usecase.SwapUseCase
import com.dhxxn17.ifourcut.ui.base.BaseUiAction
import com.dhxxn17.ifourcut.ui.base.BaseUiState
import com.dhxxn17.ifourcut.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoadingViewModel @Inject constructor(
    private val swapUseCase: SwapUseCase
): BaseViewModel() {

    val state: LoadingContract.LoadingState
        get() = state()

    val effect: Flow<LoadingContract.Effect>
        get() = effect()

    private var job: Job? = null

    init {
        initialData()
        loadData()
    }

    override fun loadData() {
        requestSwapImage()
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

    private fun requestSwapImage() {
        if (job != null && job?.isActive == true) return

        job = viewModelScope.launch {
            val response = swapUseCase.requestSwap(
                state.image.value(),
                state.image.value(),
            )

            if (response is ResultData.Success) {
                response.data?.resultImage?.let { _data ->
                    sendEffect(LoadingContract.Effect.GoToComplete(_data))
                }

            } else {
                Log.e("LoadingViewModel", "$response.message ")
            }
        }
    }

}