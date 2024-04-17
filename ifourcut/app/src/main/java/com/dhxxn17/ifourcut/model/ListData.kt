package com.dhxxn17.ifourcut.model

data class ListData(
    val title: String,
    val image: Int,
    val subTitle: String,
    val desc: String,
    val type: LIST_TYPE
)

enum class LIST_TYPE {
    PRINCESS,
    HERO
}