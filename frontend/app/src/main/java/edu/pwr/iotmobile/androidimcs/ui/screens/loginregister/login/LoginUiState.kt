package edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.login

import androidx.annotation.StringRes
import edu.pwr.iotmobile.androidimcs.R

data class LoginUiState(
    val inputFields: List<InputFieldData>
) {
    companion object {
        fun default(
            inputFields: List<InputFieldData> = emptyList()
        ) = LoginUiState(
            inputFields = inputFields
        )
    }
}

data class InputFieldData(
    val id: String,
    val text: String = "",
    @StringRes val label: Int,
    @StringRes val errorMessage: Int = R.string.s10,
    val isError: Boolean = false
)