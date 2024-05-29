package com.dhxxn17.ifourcut.ui.page.camera


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dhxxn17.ifourcut.ui.base.BaseScreen
import com.dhxxn17.ifourcut.ui.navigation.Screens
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

class CameraScreen(
    private val navController: NavController
) : BaseScreen() {

    @Composable
    fun Effect(viewModel: CameraViewModel) {
        LaunchedEffect(viewModel.effect) {
            viewModel.effect.onEach { _effect ->
                when (_effect) {
                    is CameraContract.Effect.GoToCompleteScreen -> {
                        navController.navigate(Screens.CameraCompleteScreen.route)
                    }
                }
            }.collect()
        }
    }

    @Composable
    override fun CreateContent() {
        val viewModel: CameraViewModel = hiltViewModel()

        Effect(viewModel)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            CameraScreen(
                onBackController = {
                    navController.popBackStack()
                },
                onCompleteController = { _bitmap ->
                    viewModel.sendAction(CameraContract.Action.SaveImage(_bitmap))
                }
            )
        }
    }


}