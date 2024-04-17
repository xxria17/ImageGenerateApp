package com.dhxxn17.ifourcut.ui.page.upload

import android.graphics.Bitmap
import androidx.lifecycle.viewModelScope
import com.dhxxn17.domain.usecase.SelectUserDataUseCase
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
class UploadViewModel @Inject constructor(
    private val selectUseCase: SelectUserDataUseCase,
    private val sendAllSelectDataUseCase: SendAllSelectDataUseCase
): BaseViewModel() {

    val state: UploadContract.UploadState
        get() = state()

    val effect: Flow<UploadContract.Effect>
        get() = effect()

    private var job: Job? = null

    init {
        initialData()
        loadData()
    }

    override fun loadData() {
        getSelectedCharacter()
    }

    override fun initialData() {
        state.galleryImage.sendState { null }
        state.cameraImage.sendState { "" }
    }

    override fun handleEvents(action: BaseUiAction) {
        when(action) {
            is UploadContract.Action.SetCameraImage -> {
                state.cameraImage.sendState { action.imageBitmap }
            }
            is UploadContract.Action.SetGalleryImage -> {
                state.galleryImage.sendState { action.imageUri }
            }
            is UploadContract.Action.SelectImage -> {
                selectMyImage(action.myImage)
            }
        }
    }

    override fun initialState(): BaseUiState {
        return UploadContract.UploadState(
            galleryImage = mutableCutStateOf(null),
            cameraImage = mutableCutStateOf(""),
            characterImage = mutableCutStateOf(null)
        )
    }

    private fun selectMyImage(image: Bitmap) {
        if (job != null && job?.isActive == true) return

        job = viewModelScope.launch {
            selectUseCase.invoke(image)

            sendEffect(UploadContract.Effect.GoToLoadingScreen)
        }

    }

    private fun getSelectedCharacter() {
        if (job != null && job?.isActive == true) return

        job = viewModelScope.launch {

            val data = sendAllSelectDataUseCase.invoke()
            state.characterImage.sendState { data.characterImage }
        }
    }
}