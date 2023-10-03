package edu.pwr.iotmobile.androidimcs.data

import androidx.annotation.StringRes
import edu.pwr.iotmobile.androidimcs.R

data class InputFieldData(
    val text: String = "",
    @StringRes val label: Int,
    @StringRes val errorMessage: Int = R.string.s10,
    val isError: Boolean = false
)