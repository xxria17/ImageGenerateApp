package com.dhxxn17.ifourcut.ui.page.select

import com.dhxxn17.ifourcut.ui.base.BaseUiAction
import com.dhxxn17.ifourcut.ui.base.BaseUiState
import com.dhxxn17.ifourcut.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SelectViewModel @Inject constructor(

) : BaseViewModel() {

    val state: SelectContract.SelectState
        get() = state()

    init {
        initialData()
        loadData()
    }

    override fun loadData() {
        state.imageList.sendState {
            arrayListOf<String>(
                "https://i.ibb.co/b3By60R/ref8.png",
                "https://i.ibb.co/Dkb0Fr0/ref9.png",
                "https://i.ibb.co/8mgsxHG/ref10.png",
                "https://i.ibb.co/wdnDnZb/ref2.png",
                "https://i.ibb.co/kQC7bDb/ref5.png",
                "https://i.ibb.co/thLx8kG/ref6.png",
                "https://i.ibb.co/QnfwBcN/ref7.png"
            )
        }

        state.nameList.sendState {
            arrayListOf<String>(
                "The Little Mermaid",
                "Jasmine",
                "Cinderella",
                "Snow White",
                "Spider Man",
                "Captain America",
                "SuperMan"
            )
        }
    }

    override fun initialData() {
        state.imageList.sendState { emptyList() }
        state.nameList.sendState { emptyList() }
    }

    override fun handleEvents(action: BaseUiAction) {

    }

    override fun initialState(): BaseUiState {
        return SelectContract.SelectState(
            imageList = mutableCutStateListOf<String>(emptyList()),
            nameList = mutableCutStateListOf(emptyList())
        )
    }
}