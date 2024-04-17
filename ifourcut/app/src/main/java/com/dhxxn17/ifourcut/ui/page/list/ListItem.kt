package com.dhxxn17.ifourcut.ui.page.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dhxxn17.ifourcut.ui.theme.Typography

@Composable
fun ListItem(
    title: String,
    image: Int,
    subTitle: String,
    desc: String
) {
    Column(
        modifier = Modifier
    ) {
        Text(
            text = title,
            style = Typography.titleSmall,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        Box(
            modifier = Modifier
                .aspectRatio(3f / 2f)
        ) {
            Image(
                painter = painterResource(id = image),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop,
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Black.copy(alpha = 0.2f))
            )

            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .align(Alignment.BottomStart)
            ) {
                Text(
                    text = subTitle,
                    style = Typography.bodyMedium,
                    color = Color.White
                )

                Text(
                    text = desc,
                    style = Typography.bodySmall,
                    color = Color.White
                )
            }
        }
    }
}