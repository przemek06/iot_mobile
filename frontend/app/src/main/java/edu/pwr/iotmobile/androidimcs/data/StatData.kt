package edu.pwr.iotmobile.androidimcs.data

import androidx.annotation.StringRes

data class StatData (
    @StringRes val label: Int,
    val value: String = "0"
)