package com.dhxxn17.ifourcut.ui.page.list

import com.dhxxn17.ifourcut.R
import com.dhxxn17.ifourcut.model.LIST_TYPE
import com.dhxxn17.ifourcut.model.ListData
import com.dhxxn17.ifourcut.ui.base.BaseUiAction
import com.dhxxn17.ifourcut.ui.base.BaseUiState
import com.dhxxn17.ifourcut.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(

): BaseViewModel() {

    val state: ListContract.ListState
        get() = state()

    init {
        initialData()
        loadData()
    }


    override fun loadData() {

    }

    override fun initialData() {
        state.data.sendState {
            listOf(
                ListData(
                    title = "공주",
                    image = R.drawable.list_img1,
                    subTitle = "AI 동화 공주 캐릭터",
                    desc = "동화 속 공주가 되어볼 수 있어요!",
                    type = LIST_TYPE.PRINCESS
                ),
                ListData(
                    title = "영웅",
                    image = R.drawable.list_img2,
                    subTitle = "AI 영웅 캐릭터",
                    desc = "멋있는 영웅의 모습을 경험해보세요!",
                    type = LIST_TYPE.HERO
                )
            )
        }
    }

    override fun handleEvents(action: BaseUiAction) {

    }

    override fun initialState(): BaseUiState {
        return ListContract.ListState(
            data = mutableCutStateListOf(emptyList())
        )
    }
}