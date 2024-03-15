package com.dhxxn17.ifourcut.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.dhxxn17.ifourcut.R

enum class PICTURE {
    CAMERA,
    GALLERY,
    NONE
}

@Composable
fun PhotoDialog(
    setShowDialog: (Boolean) -> Unit,
    setChooseData: (PICTURE) -> Unit
) {
    Dialog(onDismissRequest = {
        setShowDialog(false)
    }) {
        Surface(
            modifier = Modifier.width(300.dp)
                .wrapContentHeight(),
            shape = RoundedCornerShape(12.dp),
            color = Color.White
        ) {
            Column(
                Modifier.background(Color.White)
                    .padding(10.dp),
            ) {
                Text(
                    text = "선택",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                            .clickable {
                                setChooseData(PICTURE.CAMERA)
                                setShowDialog(false)
                            },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Image(
                            painter = painterResource(id = R.drawable.ic_camera),
                            contentDescription = "카메라 버튼",
                            modifier = Modifier.size(25.dp)
                        )
                        Text(
                            text= "카메라",
                            fontSize = 16.sp
                        )
                    }
                    Column(
                        modifier = Modifier.weight(1f)
                            .clickable {
                                setChooseData(PICTURE.GALLERY)
                                setShowDialog(false)
                            },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Image(
                            painter = painterResource(id = R.drawable.ic_gallery),
                            contentDescription = "갤러리 버튼",
                            modifier = Modifier.size(25.dp)
                        )
                        Text(
                            text = "갤러리",
                            fontSize = 16.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                        .clickable {
                            setShowDialog(false)
                            setChooseData(PICTURE.NONE)
                        }
                ){
                    Text(
                        "취소하기",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}