package com.dhxxn17.ifourcut.ui.page.complete

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.viewModelScope
import com.dhxxn17.domain.usecase.RequestQRUseCase
import com.dhxxn17.domain.usecase.SendAllSelectDataUseCase
import com.dhxxn17.ifourcut.ui.base.BaseUiAction
import com.dhxxn17.ifourcut.ui.base.BaseUiState
import com.dhxxn17.ifourcut.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompleteViewModel @Inject constructor(
    private val useCase: SendAllSelectDataUseCase,
    private val qrUseCase: RequestQRUseCase
): BaseViewModel() {

    val state: CompleteContract.CompleteState
        get() = state()

    val effect: Flow<CompleteContract.Effect>
        get() = effect()

    private var job: Job? = null

    init {
        initialData()
        loadData()
    }

    override fun loadData() {
        getCompleteImage()
    }

    override fun initialData() {
        state.image.sendState { null }
        state.originFaceImg.sendState { null }
        state.qrImage.sendState { null }
        state.isLoading.sendState { false }
        state.showDialog.sendState { false }
    }

    override fun handleEvents(action: BaseUiAction) {
        when(action) {
            is CompleteContract.Action.RequestQRCode -> {
                requestQRCode(action.img)
            }
            is CompleteContract.Action.SetShowDialog -> {
                state.showDialog.sendState { action.isShow }
            }
        }
    }

    private fun requestQRCode(img: Bitmap) {
        if (job != null && job?.isActive == true) return

        job = viewModelScope.launch {
            state.showDialog.sendState { true }
            state.isLoading.sendState { true }

            val result = qrUseCase.requestQR(img)
            state.isLoading.sendState { false }
            result.data?.let { _img ->
                val bitmap = BitmapFactory.decodeByteArray(_img, 0, _img.size)
                state.qrImage.sendState { bitmap }
            }
        }
    }

    private fun getCompleteImage() {
        if (job != null && job?.isActive == true) return

        job = viewModelScope.launch {
            val data = useCase.invoke()
            val bitmap = BitmapFactory.decodeByteArray(data.completeImage, 0, data.completeImage?.size?:0)
            state.originFaceImg.sendState { data.myImage }
            state.image.sendState { bitmap }
        }
    }

    override fun initialState(): BaseUiState {
        return CompleteContract.CompleteState(
            image = mutableCutStateOf(null),
            originFaceImg = mutableCutStateOf(null),
            qrImage = mutableCutStateOf(null),
            showDialog = mutableCutStateOf(false),
            isLoading = mutableCutStateOf(false)
        )
    }
}