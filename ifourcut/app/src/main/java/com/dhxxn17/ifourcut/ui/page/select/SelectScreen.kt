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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dhxxn17.ifourcut.R
import com.dhxxn17.ifourcut.ui.base.BaseScreen
import com.dhxxn17.ifourcut.ui.navigation.Screens
import com.dhxxn17.ifourcut.ui.page.CharItem
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.collect

class SelectScreen(
    private val navController: NavController
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
        val viewModel: SelectViewModel = hiltViewModel()

        Effect(viewModel)

        Box(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
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
            Column {
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
                    val name = viewModel.state.nameList.value()[page]
                    CharItem(
                        imageUrl = viewModel.state.imageList.value()[page],
                        onClick = { _imageId ->
                            ContextCompat.getDrawable(context, _imageId)?.let {
                                viewModel.sendAction(
                                    SelectContract.Action.SelectCharacter(
                                        type = name,
                                        image = it
                                    )
                                )
                            }

                        },
                        name = name
                    )

                }
            }
        }

    }

}