package edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.register

import edu.pwr.iotmobile.androidimcs.data.InputFieldData

data class RegisterUiState(
    val inputFields: Map<RegisterViewModel.InputFieldType, InputFieldData>
) {
    companion object {
        fun default(
            inputFields: Map<RegisterViewModel.InputFieldType, InputFieldData> = emptyMap()
        ) = RegisterUiState(
            inputFields = inputFields
        )
    }
}