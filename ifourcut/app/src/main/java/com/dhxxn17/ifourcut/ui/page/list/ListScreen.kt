package com.dhxxn17.ifourcut.ui.page.list

import android.app.Activity
import android.os.Build
import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dhxxn17.ifourcut.R
import com.dhxxn17.ifourcut.model.LIST_TYPE
import com.dhxxn17.ifourcut.ui.base.BaseScreen
import com.dhxxn17.ifourcut.ui.navigation.Screens

class ListScreen(
    private val navController: NavController
): BaseScreen() {

    @Composable
    override fun CreateContent() {
        val viewModel : ListViewModel = hiltViewModel()
        val view = LocalView.current

        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.White.toArgb()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
                windowInsetsController.isAppearanceLightStatusBars = true
            } else {
                // 이전 버전의 안드로이드에서는 이 방법을 사용
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.aizac_logo),
                    contentDescription = null,
                    modifier = Modifier.width(80.dp),
                )

                Spacer(modifier = Modifier.weight(1f))

//                Image(
//                    painter = painterResource(id = R.drawable.ic_my),
//                    contentDescription = null,
//                    modifier = Modifier.size(30.dp)
//                )
            }

            LazyColumn (
                modifier = Modifier.padding(top = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                viewModel.state.data.value().forEach { _data ->
                    item {
                        Box(
                            modifier = Modifier.clickable {
                                when(_data.type) {
                                    LIST_TYPE.PRINCESS -> {
                                        navController.navigate(Screens.PrincessScreen.route)
                                    }
                                    LIST_TYPE.HERO -> {
                                        navController.navigate(Screens.HeroScreen.route)
                                    }
                                }
                            }
                        ) {
                            ListItem(
                                title = _data.title,
                                image = _data.image,
                                subTitle = _data.subTitle,
                                desc = _data.desc
                            )
                        }
                    }
                }

            }
        }
    }
}