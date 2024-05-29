package com.dhxxn17.ifourcut.ui.page.camera

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dhxxn17.ifourcut.R
import com.dhxxn17.ifourcut.ui.base.BaseScreen
import com.dhxxn17.ifourcut.ui.navigation.Screens

class CameraCompleteScreen(
    private val navController: NavController
) : BaseScreen() {

    @Composable
    override fun CreateContent() {
        val viewModel: CameraCompleteViewModel = hiltViewModel()
        val scrollState = rememberScrollState()
        val interactionSource = remember {
            MutableInteractionSource()
        }
        val ripple = rememberRipple(bounded = false)
        val context = LocalContext.current

        Column(
            modifier = Modifier.fillMaxSize()
                .background(Color.White)
                .verticalScroll(scrollState)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painterResource(id = R.drawable.ic_back),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(10.dp)
                        .size(30.dp)
                        .indication(interactionSource, ripple)
                        .clickable {
                            navController.popBackStack()
                        }
                )

                Spacer(
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "확인",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier
                        .indication(interactionSource, ripple)
                        .clickable {
                            Toast.makeText(context, "잠시만 기다려주세요", Toast.LENGTH_SHORT).show()
                            navController.navigate(Screens.LoadingScreen.route)
                        }
                        .padding(10.dp)
                )
            }

            viewModel.state.image.value()?.let{ _image ->
                Image(
                    bitmap = _image.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                )
            }
        }
    }
}