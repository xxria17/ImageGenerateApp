package com.dhxxn17.ifourcut.ui.page.loading

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.dhxxn17.ifourcut.R
import com.dhxxn17.ifourcut.common.ImageUtils
import com.dhxxn17.ifourcut.ui.base.BaseScreen
import com.dhxxn17.ifourcut.ui.navigation.Screens
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

class LoadingScreen(
    private val navController: NavController
) : BaseScreen() {

    @Composable
    fun Effect(viewModel: LoadingViewModel) {

        LaunchedEffect(viewModel.effect) {
            viewModel.effect.onEach { _effect ->
                when (_effect) {
                    is LoadingContract.Effect.GoToComplete -> {
                        navController.navigate(Screens.CompleteScreen.route)
                    }
                }
            }.collect()
        }
    }

    @Composable
    override fun CreateContent() {
        val viewModel: LoadingViewModel = hiltViewModel()
        val context = LocalContext.current

        Effect(viewModel)

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


        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            Image(
                painter = painterResource(id = R.drawable.background),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.matchParentSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = 0.5f))
            )

            Column(
                modifier = Modifier.fillMaxSize()
            ) {

                Spacer(modifier = Modifier.height(80.dp))

                Text(
                    text = stringResource(id = R.string.loading_title),
                    fontWeight = FontWeight.SemiBold,
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
                            bitmap = ImageUtils.stringToBitmap(viewModel.state.image.value()).asImageBitmap(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .padding(20.dp)
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(12.dp))
                        )
                    }

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