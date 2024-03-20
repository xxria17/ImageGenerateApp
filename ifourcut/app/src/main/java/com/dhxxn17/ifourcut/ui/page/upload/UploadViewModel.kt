package com.dhxxn17.ifourcut.ui.page.upload

import com.dhxxn17.ifourcut.ui.base.BaseUiAction
import com.dhxxn17.ifourcut.ui.base.BaseUiState
import com.dhxxn17.ifourcut.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UploadViewModel @Inject constructor(

): BaseViewModel() {

    val state: UploadContract.UploadState
        get() = state()

    init {
        initialData()
        loadData()
    }

    override fun loadData() {
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
        }
    }

    override fun initialState(): BaseUiState {
        return UploadContract.UploadState(
            galleryImage = mutableCutStateOf(null),
            cameraImage = mutableCutStateOf("")
        )
    }
}