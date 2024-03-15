package com.dhxxn17.ifourcut.ui.page.select

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dhxxn17.ifourcut.R
import com.dhxxn17.ifourcut.ui.base.BaseScreen
import com.dhxxn17.ifourcut.ui.navigation.Screens
import com.dhxxn17.ifourcut.ui.page.CharItem
import com.dhxxn17.ifourcut.ui.page.StaggeredVerticalGrid
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class SelectScreen(
    private val viewModel: SelectViewModel,
    private val navController: NavController
) : BaseScreen() {

    @Composable
    override fun CreateContent() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .background(color = Color(0xffFFF8EE), shape = RoundedCornerShape(12.dp))
                        .padding(10.dp)
                        .weight(1f)
                ) {
                    Text(
                        text = "FRAME",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xffFF9800)
                    )
                    Text(
                        text = "SELECT",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xffFF9800)
                    )
                }

                Image(
                    painter = painterResource(id = R.drawable.ic_next),
                    contentDescription = "",
                    modifier = Modifier.size(50.dp),
                    colorFilter = ColorFilter.tint(Color(0xffFF9800))
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
            ) {
                viewModel.state.imageList.value().forEachIndexed { _index, _char ->
                    item {
                        CharItem(
                            imageUrl = _char,
                            name = viewModel.state.nameList.value()[_index],
                            onClick = { _imageUrl ->
                                val encodedUrl =
                                    URLEncoder.encode(_imageUrl, StandardCharsets.UTF_8.toString())
                                navController.navigate(
                                    Screens.UploadScreen.withImageUrl(
                                        encodedUrl
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }
    }

}