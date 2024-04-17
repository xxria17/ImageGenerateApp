package com.dhxxn17.ifourcut.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.dhxxn17.ifourcut.R

val pretender = FontFamily(
    Font(R.font.pretendard_black, FontWeight.Black, FontStyle.Normal),
    Font(R.font.pretendard_bold, FontWeight.Bold, FontStyle.Normal),
    Font(R.font.pretendard_extrabold, FontWeight.ExtraBold, FontStyle.Normal),
    Font(R.font.pretendard_light, FontWeight.Light, FontStyle.Normal),
    Font(R.font.pretendard_medium, FontWeight.Medium, FontStyle.Normal),
//    Font(R.font.pretendard_regular, FontWeight.Normal, FontStyle.Normal),
    Font(R.font.pretendard_semibold, FontWeight.SemiBold, FontStyle.Normal),
    Font(R.font.pretendard_thin, FontWeight.Thin, FontStyle.Normal),
    Font(R.font.pretendard_extralight, FontWeight.ExtraLight, FontStyle.Normal),
)

val Typography = Typography(
    titleLarge = TextStyle(
        fontFamily = pretender,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 35.sp
    ),
    titleMedium = TextStyle(
        fontFamily = pretender,
        fontWeight = FontWeight.SemiBold,
        fontSize = 25.sp
    ),
    titleSmall = TextStyle(
        fontFamily = pretender,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = pretender,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp
    ),
    bodySmall = TextStyle(
        fontFamily = pretender,
        fontWeight = FontWeight.Thin,
        fontSize = 16.sp
    )
)