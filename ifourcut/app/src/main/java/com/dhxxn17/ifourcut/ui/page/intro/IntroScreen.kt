package com.dhxxn17.ifourcut.ui.page.intro

import android.app.Activity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.dhxxn17.ifourcut.R
import com.dhxxn17.ifourcut.ui.base.BaseScreen
import com.dhxxn17.ifourcut.ui.navigation.Screens
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
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Black.toArgb()
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {

            AutoInfiniteImagePager(
                images = arrayListOf(
                    R.drawable.intro1,
                    R.drawable.intro2,
                    R.drawable.intro3,
                    R.drawable.intro4,
                    R.drawable.intro5,
                    R.drawable.intro6,
                    R.drawable.intro7,
                    R.drawable.intro8,
                    R.drawable.intro9,
                    R.drawable.intro10,
                    R.drawable.intro11,
                    R.drawable.intro12,
                )
            )

            Image(
                painter = painterResource(id = R.drawable.aizac_logo),
                contentDescription = "",
                modifier = Modifier
                    .padding(20.dp)
                    .align(Alignment.TopStart)
                    .height(20.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .align(Alignment.BottomStart),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.intro_title),
                    fontSize = 43.sp,
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,

                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = stringResource(id = R.string.intro_desc),
                    fontSize = 20.sp,
                    color = Color.White,
                )

                Spacer(modifier = Modifier.height(25.dp))

                Button(
                    onClick = {
                        navController.navigate(
                            Screens.StartScreen.route
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    ),
                    border = BorderStroke(1.dp, Color.White),
                ) {
                    Text(
                        text = stringResource(id = R.string.intro_next),
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.padding(10.dp)
                    )
                }
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