package com.dhxxn17.ifourcut.ui.page.loading

import com.dhxxn17.ifourcut.ui.base.BaseUiAction
import com.dhxxn17.ifourcut.ui.base.BaseUiEffect
import com.dhxxn17.ifourcut.ui.base.BaseUiState
import com.dhxxn17.ifourcut.ui.base.CutState
import com.google.android.gms.ads.rewarded.RewardedAd

class LoadingContract {

    data class LoadingState(
        val image: CutState<String>,
        val ad: CutState<RewardedAd?>,
        val isCompleted: CutState<Boolean>,
        val isAdDone: CutState<Boolean>
    ): BaseUiState

    sealed class Action: BaseUiAction {
        object JobCancel: Action()
        data class SetRewardedAd(
            val ad: RewardedAd?
        ): Action()

        object RequestSwap: Action()

        data class IsAdDone(
            val isDone: Boolean
        ): Action()
    }

    sealed class Effect: BaseUiEffect {
        object RequestFail: Effect()
    }
}