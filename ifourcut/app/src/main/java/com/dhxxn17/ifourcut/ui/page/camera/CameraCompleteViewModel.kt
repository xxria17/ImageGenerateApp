package com.dhxxn17.ifourcut.ui.page.camera

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.dhxxn17.domain.model.ResultData
import com.dhxxn17.domain.usecase.SaveSuccessImageUseCase
import com.dhxxn17.domain.usecase.SendAllSelectDataUseCase
import com.dhxxn17.domain.usecase.SwapUseCase
import com.dhxxn17.ifourcut.ui.base.BaseUiAction
import com.dhxxn17.ifourcut.ui.base.BaseUiState
import com.dhxxn17.ifourcut.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraCompleteViewModel @Inject constructor(
    private val useCase: SendAllSelectDataUseCase,
    private val swapUseCase: SwapUseCase,
    private val saveSuccessImageUseCase: SaveSuccessImageUseCase
) : BaseViewModel() {

    val state: CameraCompleteContract.CameraCompleteState
        get() = state()

    private var job: Job? = null

    init {
        initialData()
        loadData()
    }

    override fun initialState(): BaseUiState {
        return CameraCompleteContract.CameraCompleteState(
            image = mutableCutStateOf(null),
            isCompleted = mutableCutStateOf(false),
            isRequest = mutableCutStateOf(false)
        )
    }

    override fun loadData() {
        getUserImage()
    }

    override fun initialData() {
        state.image.sendState { null }
    }

    override fun handleEvents(action: BaseUiAction) {
        when (action) {
            is CameraCompleteContract.Action.JobCancel -> {
                cancelRequest()
            }

            is CameraCompleteContract.Action.RequestSwap -> {
                requestSwapImage()
            }
        }
    }

    private fun requestSwapImage() {
        if (job != null && job?.isActive == true) return

        job = viewModelScope.launch {
            state.isRequest.sendState { true }
            val data = useCase.invoke()

            if (data.myImage != null && data.characterImage != null) {
                val response = swapUseCase.requestSwap(
                    data.characterType,
                    data.myImage!!,
                    data.characterImage!!
                )

                if (response is ResultData.Success) {
                    response.data?.let { _data ->
                        saveSuccessImageUseCase.invoke(_data)
                        state.isCompleted.sendState { true }
                    }
                } else {
                    sendEffect(CameraCompleteContract.Effect.RequestFail)
                    if (response is ResultData.Error) {
                        Log.e("LoadingViewModel", "${response.errorData}")
                    }
                }
            } else {
                Log.e("LoadingViewModel", "images all null")
            }
        }
    }

    private fun cancelRequest() {
        job?.cancel()
    }

    private fun getUserImage() {
        if (job != null && job?.isActive == true) return

        job = viewModelScope.launch {
            val data = useCase.invoke()
            state.image.sendState { data.myImage }
        }
    }

}