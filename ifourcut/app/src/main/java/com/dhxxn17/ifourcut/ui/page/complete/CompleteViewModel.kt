package com.dhxxn17.ifourcut.ui.page.complete

import android.graphics.BitmapFactory
import androidx.lifecycle.viewModelScope
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
    private val useCase: SendAllSelectDataUseCase
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
    }

    override fun handleEvents(action: BaseUiAction) {

    }

    private fun getCompleteImage() {
        if (job != null && job?.isActive == true) return

        job = viewModelScope.launch {
            val data = useCase.invoke()
            val bitmap = BitmapFactory.decodeByteArray(data.completeImage, 0, data.completeImage?.size?:0)
            state.image.sendState { bitmap }
        }
    }

    override fun initialState(): BaseUiState {
        return CompleteContract.CompleteState(
            image = mutableCutStateOf(null)
        )
    }
}