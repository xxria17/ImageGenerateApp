package com.dhxxn17.ifourcut.ui.page.select

import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.dhxxn17.domain.usecase.SelectCharacterDataUseCase
import com.dhxxn17.ifourcut.R
import com.dhxxn17.ifourcut.model.LIST_TYPE
import com.dhxxn17.ifourcut.ui.base.BaseUiAction
import com.dhxxn17.ifourcut.ui.base.BaseUiState
import com.dhxxn17.ifourcut.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectViewModel @Inject constructor(
    private val selectUseCase: SelectCharacterDataUseCase
) : BaseViewModel() {

    val state: SelectContract.SelectState
        get() = state()

    val effect: Flow<SelectContract.Effect>
        get() = effect()

    private var job: Job? = null

    init {
        initialData()
        loadData()
    }

    override fun loadData() {
    }

    override fun initialData() {
        state.imageList.sendState { emptyList() }
        state.nameList.sendState { emptyList() }
    }

    override fun handleEvents(action: BaseUiAction) {
        when (action) {
            is SelectContract.Action.SelectCharacter -> {
                selectCharacter(action.type, action.image)
            }
            is SelectContract.Action.SetType -> {
                setType(action.type)
            }
        }
    }

    override fun initialState(): BaseUiState {
        return SelectContract.SelectState(
            imageList = mutableCutStateListOf<Int>(emptyList()),
            nameList = mutableCutStateListOf(emptyList())
        )
    }

    private fun setType(type: String) {
        Log.d("!!!!!!!!!", "type :: $type")
        when (type) {
            "{${LIST_TYPE.PRINCESS.name}}" -> {
                state.imageList.sendState {
                    arrayListOf<Int>(
                        R.drawable.select11,
                        R.drawable.select22,
                        R.drawable.select33,
                        R.drawable.select44
                    )
                }

                state.nameList.sendState {
                    arrayListOf<String>(
                        "Snow white",
                        "Cinderella",
                        "Jasmine",
                        "Ariel"
                    )
                }
            }
            "{${LIST_TYPE.HERO.name}}" -> {
                state.imageList.sendState {
                    arrayListOf<Int>(
                        R.drawable.select5,
                        R.drawable.select6,
                        R.drawable.select7
                    )
                }

                state.nameList.sendState {
                    arrayListOf<String>(
                        "Spider-man",
                        "Superman",
                        "Captain America"
                    )
                }
            }
        }
    }

    private fun selectCharacter(type: String, image: Drawable) {
        if (job != null && job?.isActive == true) return

        job = viewModelScope.launch {
            selectUseCase.invoke(type, image)

            sendEffect(SelectContract.Effect.GoToUploadScreen)
        }

    }
}