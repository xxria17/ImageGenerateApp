package com.dhxxn17.ifourcut.ui.page.loading

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
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

        Effect(viewModel)

        val composition by rememberLottieComposition(
            LottieCompositionSpec.RawRes(R.raw.loading)
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

        LaunchedEffect(viewModel.state.isCompleted.value()) {
            if (viewModel.state.isCompleted.value()) {
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
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = stringResource(id = R.string.loading_title),
                    fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                    fontSize = 55.sp,
                    color = colorResource(id = R.color.main_black),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 100.dp)
                )

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                        if (viewModel.state.image.value().isNotEmpty()) {
                            Image(
                                bitmap = stringToBitmap(viewModel.state.image.value()).asImageBitmap(),
                                contentDescription = null,
                                modifier = Modifier
                                    .aspectRatio(3f / 4f)
                                    .heightIn(600.dp),
                                contentScale = ContentScale.Crop
                            )
                        }

                    LottieAnimation(
                        composition = composition,
                        progress = { progress },
                        contentScale = ContentScale.Fit,
                        modifier = Modifier

                    )
                }
            }
        }
    }
}