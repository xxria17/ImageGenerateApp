package com.dhxxn17.ifourcut.ui.page.intro

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.dhxxn17.ifourcut.ui.base.BaseScreen
import com.dhxxn17.ifourcut.ui.navigation.Screens
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay

class IntroScreen(
    private val navController: NavController
): BaseScreen() {

    @Composable
    override fun CreateContent() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {

            AutoInfiniteImagePager(
                images = arrayListOf<String>(
                    "https://i.ibb.co/b3By60R/ref8.png",
                    "https://i.ibb.co/Dkb0Fr0/ref9.png",
                    "https://i.ibb.co/8mgsxHG/ref10.png",
                    "https://i.ibb.co/wdnDnZb/ref2.png",
                    "https://i.ibb.co/kQC7bDb/ref5.png",
                    "https://i.ibb.co/thLx8kG/ref6.png",
                    "https://i.ibb.co/QnfwBcN/ref7.png"
                )
            )

            Column(
                modifier = Modifier.fillMaxWidth().padding(10.dp).align(Alignment.BottomStart),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "AI 캐릭터",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 45.sp,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "내 사진으로 만화 속 캐릭터 되어보기",
                    fontSize = 16.sp,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(18.dp))

                OutlinedButton(
                    onClick = {
                              navController.navigate(
                                  Screens.SelectScreen.route
                              )
                    },
                    modifier = Modifier.fillMaxWidth().padding(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.White
                    ),
                    border = BorderStroke(1.dp, Color.White),
                ) {
                    Text(
                        text = "지금 시작하기",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalPagerApi::class)
    @Composable
    fun AutoInfiniteImagePager(
        images: List<String>, // 이미지 URL 목록
        modifier: Modifier = Modifier,
        intervalMillis: Long = 3000 // 자동으로 넘어가는 시간 간격 (3초)
    ) {
        val pagerState = rememberPagerState()

        // Coroutine을 사용하여 자동 스크롤
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
            modifier = modifier
        ) { page ->
            Image(
                painter = rememberImagePainter(images[page]),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
            )
        }
    }
}