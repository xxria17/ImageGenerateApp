package com.dhxxn17.ifourcut.ui.page

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dhxxn17.ifourcut.ui.navigation.Screens
import com.dhxxn17.ifourcut.ui.navigation.TYPE_ARG
import com.dhxxn17.ifourcut.ui.page.camera.CameraCompleteScreen
import com.dhxxn17.ifourcut.ui.page.camera.CameraScreen
import com.dhxxn17.ifourcut.ui.page.complete.CompleteScreen
import com.dhxxn17.ifourcut.ui.page.intro.IntroScreen
import com.dhxxn17.ifourcut.ui.page.list.ListScreen
import com.dhxxn17.ifourcut.ui.page.loading.LoadingScreen
import com.dhxxn17.ifourcut.ui.page.select.SelectScreen
import com.dhxxn17.ifourcut.ui.page.start.hero.HeroScreen
import com.dhxxn17.ifourcut.ui.page.start.princess.PrincessScreen
import com.dhxxn17.ifourcut.ui.page.upload.UploadScreen
import com.dhxxn17.ifourcut.ui.theme.IfourcutTheme
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IfourcutTheme {
                MainContent()
            }
        }
        MobileAds.initialize(this)
    }

    @Composable
    fun MainContent() {
        val navController = rememberNavController()
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { _innerPadding ->
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

            composable(Screens.ListScreen.route) {
                ListScreen(navController).CreateContent()
            }

            composable(Screens.PrincessScreen.route) {
                PrincessScreen(navController).CreateContent()
            }
            composable(Screens.HeroScreen.route) {
                HeroScreen(navController).CreateContent()
            }
            composable(
                route = Screens.SelectScreen.route,
                arguments = listOf(
                    navArgument(TYPE_ARG) {
                        type = NavType.StringType
                    }
                )
            ) { _backStackEntry ->
                _backStackEntry.arguments?.getString(TYPE_ARG)?.let { _type ->
                    SelectScreen(navController, _type).CreateContent()
                }
            }
            composable(
                route = Screens.UploadScreen.route
            ) {
                UploadScreen(navController).CreateContent()
            }
            composable(
                route = Screens.LoadingScreen.route
            ) {
                LoadingScreen(navController).CreateContent()
            }
            composable(
                route = Screens.CompleteScreen.route
            ) {
                CompleteScreen(navController).CreateContent()
            }

            composable(
                route = Screens.CameraScreen.route
            ) {
                CameraScreen(navController).CreateContent()
            }

            composable(
                route = Screens.CameraCompleteScreen.route
            ) {
                CameraCompleteScreen(navController).CreateContent()
            }
        }
    }
}

