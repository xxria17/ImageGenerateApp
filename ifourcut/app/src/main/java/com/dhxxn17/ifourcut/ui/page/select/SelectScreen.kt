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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
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
            modifier = Modifier.background(Color.Black).fillMaxSize()
                .padding(12.dp)
        ) {
            Text(
                text = "아이네컷",
                fontSize = 25.sp,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(30.dp))

            LazyColumn {
                items(SelectContract.TYPE.values().size) { _index ->

                    Text(
                        text = SelectContract.TYPE.values()[_index].title,
                        fontSize = 20.sp,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )

                    LazyRow {
                        items(viewModel.state.data.value().filter { it.second == SelectContract.TYPE.values()[_index] }) { (imageUrl, _) ->
                            CharItem(
                                imageUrl = imageUrl,
                                onClick = { _imageUrl ->
                                    val encodedUrl = URLEncoder.encode(_imageUrl, StandardCharsets.UTF_8.toString())
                                    navController.navigate(Screens.UploadScreen.withImageUrl(encodedUrl))
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(25.dp))

                }
            }
        }
    }

}