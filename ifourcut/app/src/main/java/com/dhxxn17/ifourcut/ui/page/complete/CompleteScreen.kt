package com.dhxxn17.ifourcut.ui.page.complete

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.dhxxn17.ifourcut.R
import com.dhxxn17.ifourcut.ui.base.BaseScreen

class CompleteScreen (
    private val navController: NavController,
    private val imageUrl: String
): BaseScreen() {

    @Composable
    override fun CreateContent() {
        val viewModel: CompleteViewModel = hiltViewModel()

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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.Top
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "",
                        modifier = Modifier
                            .size(28.dp)
                            .clickable {
                                //TODO
                                navController.popBackStack()
                                navController.popBackStack()
                            }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Image(
                    painter = painterResource(id = R.drawable.preview),
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(400.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 24.dp,
                            bottom = 36.dp
                        )
                ) {
                    Button(
                        onClick = {

                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(58.dp),
                        shape = RoundedCornerShape(100.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor= colorResource(id = R.color.main_white),
                            contentColor= colorResource(id = R.color.main_white),
                            disabledContainerColor= colorResource(id = R.color.main_white),
                            disabledContentColor= colorResource(id = R.color.main_white),
                        )

                    ) {
                        Text(
                            text = stringResource(id = R.string.complete_save),
                            fontSize = 16.sp,
                            color = colorResource(id = R.color.main_pink),
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = {

                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(58.dp),
                        shape = RoundedCornerShape(100.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor= colorResource(id = R.color.main_pink),
                            contentColor= colorResource(id = R.color.main_pink),
                            disabledContainerColor= colorResource(id = R.color.main_white),
                            disabledContentColor= colorResource(id = R.color.main_white),
                        )

                    ) {
                        Text(
                            text = stringResource(id = R.string.complete_share),
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }

    }

}