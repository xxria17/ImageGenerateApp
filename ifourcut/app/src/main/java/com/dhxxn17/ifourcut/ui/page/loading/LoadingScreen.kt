package com.dhxxn17.ifourcut.ui.page.loading

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.dhxxn17.ifourcut.R
import com.dhxxn17.ifourcut.ui.base.BaseScreen
import com.dhxxn17.ifourcut.ui.navigation.Screens
import com.dhxxn17.ifourcut.ui.page.upload.ImageTypeForView
import kotlinx.coroutines.delay
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

class LoadingScreen(
    private val navController: NavController,
    private val imageUrl: String
): BaseScreen() {

    @Composable
    override fun CreateContent() {
        val viewModel: LoadingViewModel = viewModel()
        val filePath = imageUrl.replace("{", "").replace("}", "")
        val parts = filePath.split(",")

        Log.d("!!!!!", "parts :: ${parts[1]}")

        val type = if (parts.size > 1 && parts[1] == ImageTypeForView.PhotoShoot.name) ImageTypeForView.PhotoShoot
            else ImageTypeForView.Gallery

        Log.d("!!!!!", "type :: $type")



        // TODO: 임시 코드
        LaunchedEffect(key1 = true) {
            delay(5000) // 5초 지연
            navController.navigate(Screens.CompleteScreen.route)
        }


        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
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
                modifier = Modifier.fillMaxSize()
            ) {

                Spacer(modifier = Modifier.height(80.dp))

                val decodedImage = if (type == ImageTypeForView.Gallery) decodeBase64ToBitmap(parts[0])
                 else BitmapFactory.decodeFile(URLDecoder.decode(parts[0], StandardCharsets.UTF_8.toString()))
                Log.d("!!!!!", "decodedImage :: $decodedImage")
                decodedImage?.let { data ->
                    Image(
                        bitmap = data.asImageBitmap(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(20.dp)
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(12.dp))
                    )
                }

                Spacer(modifier = Modifier.height(50.dp))

                Text(
                    text = "캐릭터로 변신 중",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 25.sp,
                    color = Color(0xff242323),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }


        }
    }

    private fun decodeBase64ToBitmap(encodedImage: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(encodedImage, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: IllegalArgumentException) {
            // Base64 문자열이 유효하지 않은 경우 예외 처리
            null
        }
    }
}