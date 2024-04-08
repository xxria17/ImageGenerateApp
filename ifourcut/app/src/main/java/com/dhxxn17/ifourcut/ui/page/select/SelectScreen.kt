package com.dhxxn17.ifourcut.ui.page.select

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dhxxn17.ifourcut.R
import com.dhxxn17.ifourcut.ui.base.BaseScreen
import com.dhxxn17.ifourcut.ui.navigation.Screens
import com.dhxxn17.ifourcut.ui.page.CharItem
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class SelectScreen(
    private val viewModel: SelectViewModel,
    private val navController: NavController
) : BaseScreen() {

    @OptIn(ExperimentalPagerApi::class)
    @Composable
    override fun CreateContent() {
        Box(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
                .padding(12.dp)
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


            ) {
                Text(
                    text = stringResource(id = R.string.app_name),
                    fontSize = 25.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = stringResource(id = R.string.select_title),
                    fontSize = 18.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )

                Spacer(modifier = Modifier.height(30.dp))

                val pagerState = rememberPagerState()

                HorizontalPager(
                    count = viewModel.state.imageList.value().size,
                    state = pagerState
                ) { page ->
                    CharItem(
                        imageUrl = viewModel.state.imageList.value()[page],
                        onClick = { _imageUrl ->
                            val encodedUrl =
                                URLEncoder.encode(_imageUrl, StandardCharsets.UTF_8.toString())
                            navController.navigate(Screens.UploadScreen.withImageUrl(encodedUrl))
                        },
                        name = viewModel.state.nameList.value()[page]
                    )

                }
            }
        }

    }

}