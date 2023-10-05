package edu.pwr.iotmobile.androidimcs.data

data class MenuOption(
    val titleId: Int,
    val isBold: Boolean = false,
    val onClick: () -> Unit = {}
)
