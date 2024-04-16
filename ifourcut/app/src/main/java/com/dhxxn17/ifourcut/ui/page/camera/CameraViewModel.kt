package com.dhxxn17.ifourcut.ui.page.camera

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.dhxxn17.domain.usecase.SelectUserDataUseCase
import com.dhxxn17.ifourcut.ui.base.BaseUiAction
import com.dhxxn17.ifourcut.ui.base.BaseUiState
import com.dhxxn17.ifourcut.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val selectUserDataUseCase: SelectUserDataUseCase
): BaseViewModel() {

    val state: CameraContract.CameraState
        get() = state()

    val effect: Flow<CameraContract.Effect>
        get() = effect()

    private var job: Job? = null

    init {
        initialData()
        loadData()
    }

    override fun loadData() {

    }

    override fun initialData() {
        state.image.sendState { null }
    }

    override fun handleEvents(action: BaseUiAction) {
        when(action) {
            is CameraContract.Action.SaveImage -> {
                saveCameraImage(action.image)
            }
        }
    }

    override fun initialState(): BaseUiState {
        return CameraContract.CameraState(
            image = mutableCutStateOf(null)
        )
    }

    private fun saveCameraImage(image: Bitmap?) {
        if (job != null && job?.isActive == true) return

        job = viewModelScope.launch {
            image?.let {
                selectUserDataUseCase.invoke(it)
            }

            sendEffect(CameraContract.Effect.GoToCompleteScreen)
        }
    }
}