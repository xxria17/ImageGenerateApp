package com.dhxxn17.ifourcut.common

import android.app.Activity
import android.os.Build
import android.view.View
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat

fun setStatusBarColor(view: View, color: Color) {
    val window = (view.context as Activity).window
    window.statusBarColor = color.toArgb()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.isAppearanceLightStatusBars = true
    } else {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
}