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
import androidx.compose.foundation.layout.height
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
import com.dhxxn17.ifourcut.common.setStatusBarColor
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
            setStatusBarColor(view, Color.White)
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
                    modifier = Modifier.height(60.dp),
                )

                Spacer(modifier = Modifier.weight(1f))

            }

            LazyColumn (
                modifier = Modifier.padding(top = 40.dp),
                verticalArrangement = Arrangement.spacedBy(25.dp)
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
                            )
                        }
                    }
                }

            }
        }
    }
}