package com.dhxxn17.ifourcut.ui.page.camera


import android.graphics.Bitmap
import android.util.Base64
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.navigation.NavController
import com.dhxxn17.ifourcut.ui.base.BaseScreen
import com.dhxxn17.ifourcut.ui.navigation.Screens
import java.io.ByteArrayOutputStream

class CameraScreen(
    private val navController: NavController
): BaseScreen() {


    @Composable
    override fun CreateContent() {
        val lifecycleOwner = LocalLifecycleOwner.current

        Column(
            modifier = Modifier.fillMaxSize()
                .background(Color.White)
        ) {

            CameraPreviewWithCaptureButton(
                lifecycleOwner = lifecycleOwner,
                onClickListener = { bitmap ->
                    val baos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
                    val byteArray = baos.toByteArray()
                    val encoded = Base64.encodeToString(byteArray, Base64.DEFAULT)
                    navController.navigate(Screens.LoadingScreen.withImageUrl(encoded))
                }
            )
        }

    }


}