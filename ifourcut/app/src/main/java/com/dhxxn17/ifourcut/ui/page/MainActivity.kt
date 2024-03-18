package com.dhxxn17.ifourcut.ui.page

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dhxxn17.ifourcut.ui.navigation.IMAGE_URL_ARG
import com.dhxxn17.ifourcut.ui.navigation.Screens
import com.dhxxn17.ifourcut.ui.page.complete.CompleteScreen
import com.dhxxn17.ifourcut.ui.page.complete.CompleteViewModel
import com.dhxxn17.ifourcut.ui.page.intro.IntroScreen
import com.dhxxn17.ifourcut.ui.page.select.SelectScreen
import com.dhxxn17.ifourcut.ui.page.select.SelectViewModel
import com.dhxxn17.ifourcut.ui.page.upload.UploadScreen
import com.dhxxn17.ifourcut.ui.page.upload.UploadViewModel
import com.dhxxn17.ifourcut.ui.theme.IfourcutTheme
import com.google.accompanist.pager.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val selectViewModel: SelectViewModel by viewModels()
    private val uploadViewModel: UploadViewModel by viewModels()
    private val completeViewModel: CompleteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IfourcutTheme {
                MainContent()
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MainContent() {
        val navController = rememberNavController()

        Scaffold { _innerPadding ->
            Box(
                modifier = Modifier
                    .padding(_innerPadding)
                    .background(Color.White)
            ) {
                NavigationGraph(navController = navController)
            }
        }
    }

    @Composable
    fun NavigationGraph(navController: NavHostController) {
        NavHost(navController = navController, startDestination = Screens.IntroScreen.route) {
            composable(Screens.IntroScreen.route) {
                IntroScreen(navController).CreateContent()
            }
            composable(Screens.SelectScreen.route) {
                SelectScreen(selectViewModel, navController).CreateContent()
            }
            composable(
                route = Screens.UploadScreen.route,
                arguments = listOf(
                    navArgument(IMAGE_URL_ARG) {
                        type = NavType.StringType
                    }
                )
            ) { _backStackEntry ->
                _backStackEntry.arguments?.getString(IMAGE_URL_ARG)?.let { _imageUrl ->
//                    UploadScreen(uploadViewModel, navController, _imageUrl).CreateContent()
                    UploadScreen(navController, _imageUrl).CreateContent()
                }

            }
            composable(
                route = Screens.CompleteScreen.route,
                arguments = listOf(
                    navArgument(IMAGE_URL_ARG) {
                        type = NavType.StringType
                    }
                )
            ) { _backStackEntry ->
                _backStackEntry.arguments?.getString(IMAGE_URL_ARG)?.let { _imageUrl ->
                    CompleteScreen(completeViewModel, navController, _imageUrl).CreateContent()
                }

            }
        }
    }

}

