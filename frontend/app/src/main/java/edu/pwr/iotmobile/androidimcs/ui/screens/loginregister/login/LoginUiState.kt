package edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.login

import edu.pwr.iotmobile.androidimcs.data.InputFieldData

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