package edu.pwr.iotmobile.androidimcs.data

import androidx.annotation.StringRes
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.focus.FocusRequester
import edu.pwr.iotmobile.androidimcs.R

data class InputFieldData(
    val text: String = "",
    @StringRes val label: Int = R.string.name,
    @StringRes val errorMessage: Int = R.string.s10,
    val isError: Boolean = false,
    val keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    val focusRequester: FocusRequester = FocusRequester()
)