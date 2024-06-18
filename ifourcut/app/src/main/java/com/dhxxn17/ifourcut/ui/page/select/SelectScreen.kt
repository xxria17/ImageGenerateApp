package com.dhxxn17.ifourcut.ui.page.select

import android.app.Activity
import android.os.Build
import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dhxxn17.ifourcut.R
import com.dhxxn17.ifourcut.common.setStatusBarColor
import com.dhxxn17.ifourcut.ui.base.BaseScreen
import com.dhxxn17.ifourcut.ui.navigation.Screens
import com.dhxxn17.ifourcut.ui.page.CharItem
import com.dhxxn17.ifourcut.ui.theme.Typography
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.collect

class SelectScreen(
    private val navController: NavController,
    private val type: String
) : BaseScreen() {

    @Composable
    fun Effect(viewModel: SelectViewModel) {
        LaunchedEffect(viewModel.effect) {
            viewModel.effect.onEach { _effect ->
                when (_effect) {
                    is SelectContract.Effect.GoToUploadScreen -> {
                        navController.navigate(Screens.UploadScreen.route)
                    }
                }
            }.collect()
        }
    }

    @OptIn(ExperimentalPagerApi::class)
    @Composable
    override fun CreateContent() {
        val context = LocalContext.current
        val view = LocalView.current
        val viewModel: SelectViewModel = hiltViewModel()
        val scrollState = rememberScrollState()

        LaunchedEffect(Unit) {
            viewModel.sendAction(SelectContract.Action.SetType(type))
        }

        Effect(viewModel)

        SideEffect {
            setStatusBarColor(view, Color.White)
        }

        Box(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize().verticalScroll(scrollState)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){

                    Spacer(Modifier.height(50.dp))

                    Text(
                        text = stringResource(id = R.string.select_title),
                        fontFamily = FontFamily(Font(R.font.pretendard_extrabold)),
                        color = Color.Black,
                        fontSize = 55.sp,
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 80.dp, bottom = 5.dp)
                    )

                    Text(
                        text = stringResource(id = R.string.select_description),
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        color = Color.Black,
                        fontSize = 50.sp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }

                val pagerState = rememberPagerState()

                Spacer(Modifier.weight(1f))

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center
                ) {
                    HorizontalPager(
                        count = viewModel.state.imageList.value().size,
                        state = pagerState,
                        contentPadding = PaddingValues(horizontal = 54.dp),
                        modifier = Modifier.fillMaxSize()
                    ) { page ->
                        val name = viewModel.state.nameList.value()[page]
                        CharItem(
                            imageUrl =  viewModel.state.imageList.value()[page],
                            name = name,
                            onClick = {_imageId ->
                                ContextCompat.getDrawable(context, _imageId)?.let {
                                    viewModel.sendAction(
                                        SelectContract.Action.SelectCharacter(
                                            type = name,
                                            image = it
                                        )
                                    )
                                }
                            }
                        )
                    }
                }

                Spacer(Modifier.weight(1f))
            }
        }

    }

}