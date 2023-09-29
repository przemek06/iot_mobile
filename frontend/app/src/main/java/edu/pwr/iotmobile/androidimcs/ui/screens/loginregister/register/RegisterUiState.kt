package edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.register

import edu.pwr.iotmobile.androidimcs.data.InputFieldData

data class RegisterUiState(
    val inputFields: List<InputFieldData>
) {
    companion object {
        fun default(
            inputFields: List<InputFieldData> = emptyList()
        ) = RegisterUiState(
            inputFields = inputFields
        )
    }
}