package com.dhxxn17.ifourcut.ui.page.loading

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.dhxxn17.domain.model.ResultData
import com.dhxxn17.domain.usecase.SendAllSelectDataUseCase
import com.dhxxn17.domain.usecase.SwapUseCase
import com.dhxxn17.ifourcut.common.ImageUtils
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
    private val swapUseCase: SwapUseCase,
    private val selectUseCase: SendAllSelectDataUseCase
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
            val data = selectUseCase.invoke()
            val image = data.myImage?.let { ImageUtils.bitmapToString(it) } ?: ""
            state.image.sendState { image }

            if (data.myImage != null && data.characterImage != null) {
                val response = swapUseCase.requestSwap(
                    data.characterType,
                    data.myImage!!,
                    data.characterImage!!
                )

                if (response is ResultData.Success) {
                    response.data?.resultImage?.let { _data ->
                        sendEffect(LoadingContract.Effect.GoToComplete(_data))
                    }

                } else {
                    // TODO Fail 처리
                    Log.e("LoadingViewModel", "$response.message ")
                }
            }
        }
    }

}