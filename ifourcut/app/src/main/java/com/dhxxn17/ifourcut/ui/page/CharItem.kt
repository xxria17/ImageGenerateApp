package com.dhxxn17.ifourcut.ui.page

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun CharItem(
    imageUrl: String,
    name: String,
    onClick: (String) -> Unit
) {
    Box() {
        AsyncImage(
            model = imageUrl,
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxHeight(0.9f)
                .fillMaxWidth()
                .padding(10.dp)
                .clip(RoundedCornerShape(10.dp))
                .clickable {
                    onClick.invoke(imageUrl)
                }
        )

        Box(
            modifier = Modifier
                .fillMaxHeight(0.9f)
                .fillMaxWidth()
                .padding(10.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.Black.copy(alpha = 0.1f))
        )

        Text(
            text = name,
            fontSize = 23.sp,
            color = Color.White,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 30.dp)
                .align(Alignment.BottomStart)
        )

    }

}