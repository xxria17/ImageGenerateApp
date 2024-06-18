package com.dhxxn17.ifourcut.ui.page.camera

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
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
import com.dhxxn17.ifourcut.common.bitmapToString
import com.dhxxn17.ifourcut.ui.base.BaseScreen
import com.dhxxn17.ifourcut.ui.navigation.Screens

class CameraCompleteScreen(
    private val navController: NavController
) : BaseScreen() {

    @Composable
    override fun CreateContent() {
        val viewModel: CameraCompleteViewModel = hiltViewModel()
        val scrollState = rememberScrollState()
        val configuration = LocalConfiguration.current
        val screenHeight = configuration.screenHeightDp.dp
        val buttonHeight = screenHeight / 10
        val context = LocalContext.current

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


        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (viewModel.state.isRequest.value()) {
                Text(
                    text = stringResource(id = R.string.loading_title),
                    fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                    fontSize = 55.sp,
                    color = colorResource(id = R.color.main_black),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 100.dp)
                )
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 30.dp, horizontal = 10.dp)
                        .padding(horizontal = 30.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .background(Color(0xffED6945))
                        .height(buttonHeight)
                        .clickable {
                            viewModel.sendAction(CameraCompleteContract.Action.RequestSwap)
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "확인",
                        color = Color.White,
                        fontSize = 50.sp,
                        fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                        modifier = Modifier.padding(10.dp),
                    )
                }
            }

            Spacer(Modifier.height(40.dp))

            Box(
                modifier = Modifier
                    .aspectRatio(3f/ 4f)
                    .heightIn(600.dp),
                contentAlignment = Alignment.Center
            ) {

                viewModel.state.image.value()?.let { _image ->
                    Image(
                        bitmap = _image.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                if (viewModel.state.isRequest.value()) {
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