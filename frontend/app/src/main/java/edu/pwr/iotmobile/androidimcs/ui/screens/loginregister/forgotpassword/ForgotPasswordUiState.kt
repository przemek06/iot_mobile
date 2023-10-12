package edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.forgotpassword

import edu.pwr.iotmobile.androidimcs.data.InputFieldData

data class ForgotPasswordUiState(
    val inputFields: Map<ForgotPasswordViewModel.InputFieldType, InputFieldData>,
    val isInputCode: Boolean
) {
    companion object {
        fun default(
            inputFields: Map<ForgotPasswordViewModel.InputFieldType, InputFieldData> = emptyMap(),
            isInputCode: Boolean = false,
        ) = ForgotPasswordUiState(
            inputFields = inputFields,
            isInputCode = isInputCode
        )
    }
}