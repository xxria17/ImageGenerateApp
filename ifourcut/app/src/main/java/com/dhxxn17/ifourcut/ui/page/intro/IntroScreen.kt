package com.dhxxn17.ifourcut.ui.page.intro

import android.app.Activity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.dhxxn17.ifourcut.R
import com.dhxxn17.ifourcut.common.setStatusBarColor
import com.dhxxn17.ifourcut.ui.base.BaseScreen
import com.dhxxn17.ifourcut.ui.navigation.Screens
import com.dhxxn17.ifourcut.ui.theme.Typography
import com.dhxxn17.ifourcut.ui.theme.pretender
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay

class IntroScreen(
    private val navController: NavController
) : BaseScreen() {

    @Composable
    override fun CreateContent() {
        val view = LocalView.current
        val configuration = LocalConfiguration.current
        val screenHeight = configuration.screenHeightDp.dp
        val buttonHeight = screenHeight / 10
        SideEffect {
            setStatusBarColor(view, Color.Black)
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {


            AutoInfiniteImagePager(
                images = arrayListOf(
                    R.drawable.result3,
                    R.drawable.result4,
                    R.drawable.result5,
                    R.drawable.result6,
                    R.drawable.result7,
                )
            )

            Image(
                painter = painterResource(id = R.drawable.aizac_logo),
                contentDescription = "",
                modifier = Modifier
                    .padding(start = 30.dp, top = 30.dp)
                    .height(80.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .align(Alignment.BottomStart),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(32.dp))
                        .background(Color(0xFF0A310C))
                        .height(buttonHeight)
                        .clickable {
                            navController.navigate(
                                Screens.ListScreen.route
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.intro_next),
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                        fontSize = 55.sp,
                        modifier = Modifier.padding(10.dp)
                    )
                }
                Spacer(modifier = Modifier.height(25.dp))

            }
        }
    }

    @OptIn(ExperimentalPagerApi::class)
    @Composable
    fun AutoInfiniteImagePager(
        images: List<Int>,
        modifier: Modifier = Modifier,
        intervalMillis: Long = 3000
    ) {
        val pagerState = rememberPagerState()

        LaunchedEffect(pagerState) {
            val pageCount = images.size
            if (pageCount > 0) {
                while (true) {
                    delay(intervalMillis)
                    val nextPage = (pagerState.currentPage + 1) % pageCount
                    pagerState.animateScrollToPage(nextPage)
                }
            }
        }

        HorizontalPager(
            count = images.size,
            state = pagerState,
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) { page ->
            Image(
                painter = rememberImagePainter(images[page]),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.2f))
            )
        }
    }
}