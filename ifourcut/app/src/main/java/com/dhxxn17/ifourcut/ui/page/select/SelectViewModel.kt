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
                "인어공주",
                "자스민",
                "신데렐라",
                "백설공주",
                "스파이더맨",
                "캡틴 아메리카",
                "슈퍼맨"
            )
        }

        state.data.sendState {
            arrayListOf(
                "https://i.ibb.co/b3By60R/ref8.png" to SelectContract.TYPE.PRINCESS,
                "https://i.ibb.co/Dkb0Fr0/ref9.png" to SelectContract.TYPE.PRINCESS,
                "https://i.ibb.co/8mgsxHG/ref10.png" to SelectContract.TYPE.PRINCESS,
                "https://i.ibb.co/wdnDnZb/ref2.png" to SelectContract.TYPE.PRINCESS,
                "https://i.ibb.co/kQC7bDb/ref5.png" to SelectContract.TYPE.HERO,
                "https://i.ibb.co/thLx8kG/ref6.png" to SelectContract.TYPE.HERO,
                "https://i.ibb.co/QnfwBcN/ref7.png" to SelectContract.TYPE.HERO
            )
        }
    }

    override fun initialData() {
        state.imageList.sendState { emptyList() }
        state.nameList.sendState { emptyList() }
        state.data.sendState { emptyList() }
    }

    override fun handleEvents(action: BaseUiAction) {

    }

    override fun initialState(): BaseUiState {
        return SelectContract.SelectState(
            imageList = mutableCutStateListOf<String>(emptyList()),
            nameList = mutableCutStateListOf(emptyList()),
            data = mutableCutStateListOf(emptyList())
        )
    }
}