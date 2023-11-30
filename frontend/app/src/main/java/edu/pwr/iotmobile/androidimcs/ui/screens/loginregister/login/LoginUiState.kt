package edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.login

import edu.pwr.iotmobile.androidimcs.data.InputFieldData

data class LoginUiState(
    val inputFields: Map<LoginViewModel.InputFieldType, InputFieldData>,
    val isLoading: Boolean = false
) {
    companion object {
        fun default(
            inputFields: Map<LoginViewModel.InputFieldType, InputFieldData> = emptyMap()
        ) = LoginUiState(
            inputFields = inputFields
        )
    }
}