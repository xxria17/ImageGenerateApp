package com.dhxxn17.ifourcut.ui.page.loading

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.dhxxn17.domain.model.ResultData
import com.dhxxn17.domain.usecase.SaveSuccessImageUseCase
import com.dhxxn17.domain.usecase.SendAllSelectDataUseCase
import com.dhxxn17.domain.usecase.SwapUseCase
import com.dhxxn17.ifourcut.common.bitmapToString
import com.dhxxn17.ifourcut.ui.base.BaseUiAction
import com.dhxxn17.ifourcut.ui.base.BaseUiState
import com.dhxxn17.ifourcut.ui.base.BaseViewModel
import com.google.android.gms.ads.rewarded.RewardedAd
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoadingViewModel @Inject constructor(
    private val swapUseCase: SwapUseCase,
    private val sendAllSelectDataUseCase: SendAllSelectDataUseCase,
    private val saveSuccessImageUseCase: SaveSuccessImageUseCase
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
        state.ad.sendState { null }
        state.isCompleted.sendState { false }
        state.isAdDone.sendState { false }
    }

    override fun handleEvents(action: BaseUiAction) {
        when(action) {
            is LoadingContract.Action.JobCancel -> {
                cancelRequest()
            }
            is LoadingContract.Action.SetRewardedAd -> {
                setInterstitialAd(action.ad)
            }
            is LoadingContract.Action.RequestSwap -> {
                requestSwapImage()
            }
            is LoadingContract.Action.IsAdDone -> {
                state.isAdDone.sendState { action.isDone }
            }
        }
    }

    override fun initialState(): BaseUiState {
        return LoadingContract.LoadingState(
            image = mutableCutStateOf(""),
            ad = mutableCutStateOf(null),
            isCompleted = mutableCutStateOf(false),
            isAdDone = mutableCutStateOf(false)
        )
    }

    private fun setInterstitialAd(ad: RewardedAd?) {
        state.ad.sendState { ad }
    }

    private fun requestSwapImage() {
        if (job != null && job?.isActive == true) return

        job = viewModelScope.launch {
            val data = sendAllSelectDataUseCase.invoke()
            val image = data.myImage?.let { bitmapToString(it) } ?: ""
            state.image.sendState { image }

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
                    sendEffect(LoadingContract.Effect.RequestFail)
                    Log.e("LoadingViewModel", "$response.message ")
                }
            }
        }
    }

    private fun cancelRequest() {
        job?.cancel()
    }

}