package com.dhxxn17.ifourcut.ui.page

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun CharItem(
    imageUrl: String,
    onClick: (String) -> Unit
) {
    AsyncImage(
        model = imageUrl,
        contentDescription = "",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .heightIn(200.dp)
            .aspectRatio(3 / 2f)
            .padding(5.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable {
                onClick.invoke(imageUrl)
            }
    )
}