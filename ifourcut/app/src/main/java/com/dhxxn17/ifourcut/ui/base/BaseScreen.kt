package com.dhxxn17.ifourcut.ui.base

import androidx.compose.runtime.Composable

abstract class BaseScreen {
    fun <T> CutState<T>.value(): T {
        return this.getValue(this@BaseScreen)
    }

    fun <T> CutStateList<T>.value(): List<T> {
        return this.getValue(this@BaseScreen)
    }

    @Composable
    abstract fun CreateContent()
}