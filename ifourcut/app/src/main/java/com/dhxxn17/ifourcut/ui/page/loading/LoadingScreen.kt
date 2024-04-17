package com.dhxxn17.ifourcut.ui.page.loading

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieAnimatable
import com.airbnb.lottie.compose.rememberLottieComposition
import com.dhxxn17.ifourcut.BuildConfig
import com.dhxxn17.ifourcut.R
import com.dhxxn17.ifourcut.common.stringToBitmap
import com.dhxxn17.ifourcut.ui.base.BaseScreen
import com.dhxxn17.ifourcut.ui.base.findActivity
import com.dhxxn17.ifourcut.ui.navigation.Screens
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

class LoadingScreen(
    private val navController: NavController
) : BaseScreen() {

    private val TAG = "LoadingScreen"

    @Composable
    fun Effect(viewModel: LoadingViewModel) {
        val context = LocalContext.current

        LaunchedEffect(viewModel.effect) {
            viewModel.effect.onEach { _effect ->
                when (_effect) {
                    is LoadingContract.Effect.RequestFail -> {
                        Toast.makeText(context, "실패하였습니다. 다시 시도해주세요", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    }
                }
            }.collect()
        }
    }

    @Composable
    override fun CreateContent() {
        val viewModel: LoadingViewModel = hiltViewModel()
        val scrollState = rememberScrollState()
        val context = LocalContext.current
        var adRequest : AdRequest? = null

        Effect(viewModel)

        // 광고 객체 초기화
        LaunchedEffect(Unit) {
            adRequest = AdRequest.Builder().build()

            adRequest?.let {
                RewardedAd.load(
                    context,
                    BuildConfig.ADMOB_AD_REWARD_ID_TEST,
                    it,
                    object : RewardedAdLoadCallback() {
                        override fun onAdFailedToLoad(adError: LoadAdError) {
                            Log.e(TAG, "$adError")
                            viewModel.sendAction(LoadingContract.Action.SetRewardedAd(null))
                        }

                        override fun onAdLoaded(rewardedAd: RewardedAd) {
                            Log.d(TAG, "Ad was loaded.")
                            viewModel.sendAction(LoadingContract.Action.SetRewardedAd(rewardedAd))
                        }
                    })
            }

        }


        LaunchedEffect(viewModel.state.ad.value()) {
            viewModel.state.ad.value()?.fullScreenContentCallback =
                object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        // 보상형 광고가 닫힐 때
                        Log.d(TAG, "Ad dismissed fullscreen content.")
                        viewModel.sendAction(LoadingContract.Action.SetRewardedAd(null))
                    }

                    override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                        // 광고 표시에 실패한 경우
                        Log.e(TAG, "onAdFailedToShowFullScreenContent error : $p0")
                        viewModel.sendAction(LoadingContract.Action.SetRewardedAd(null))
                        Toast.makeText(context, "다시 시도해주세요", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    }
                }


            viewModel.state.ad.value()?.let { ad ->
                ad.show(context.findActivity(), OnUserEarnedRewardListener { rewardItem ->
                    val rewardAmount = rewardItem.amount
                    val rewardType = rewardItem.type
                    viewModel.sendAction(LoadingContract.Action.IsAdDone(true))
                    Log.d(TAG, "User earned the reward.")
                })
            } ?: run {
                Log.d(TAG, "The rewarded ad wasn't ready yet.")
            }
        }

        val composition by rememberLottieComposition(
            LottieCompositionSpec.RawRes(R.raw.loading_anim)
        )
        val lottieAnimatable = rememberLottieAnimatable()

        val progress by animateLottieCompositionAsState(
            composition,
            isPlaying = true,
            clipSpec = LottieClipSpec.Frame(0, 42),
            iterations = LottieConstants.IterateForever,
            reverseOnRepeat = false,
            restartOnPlay = false
        )


        LaunchedEffect(composition) {
            lottieAnimatable.animate(
                composition = composition,
                clipSpec = LottieClipSpec.Frame(0, 1200),
                initialProgress = 0f
            )
        }

        DisposableEffect(Unit) {
            onDispose {
                viewModel.sendAction(LoadingContract.Action.JobCancel)
            }
        }

        LaunchedEffect(viewModel.state.isCompleted.value(), viewModel.state.isAdDone.value()) {
            if (viewModel.state.isCompleted.value() && viewModel.state.isAdDone.value()) {
                navController.navigate(Screens.CompleteScreen.route)
            }
        }


        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {

                Spacer(modifier = Modifier.height(80.dp))

                Text(
                    text = stringResource(id = R.string.loading_title),
                    fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                    fontSize = 25.sp,
                    color = colorResource(id = R.color.main_black),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (viewModel.state.image.value().isNotEmpty()) {
                        Image(
                            bitmap = stringToBitmap(viewModel.state.image.value()).asImageBitmap(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .padding(20.dp)
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(12.dp))
                        )

                        LottieAnimation(
                            composition = composition,
                            progress = { progress },
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .aspectRatio(1f)
                        )
                    }
                }

            }


        }
    }

}