package edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.forgotpassword

import edu.pwr.iotmobile.androidimcs.data.InputFieldData

data class ForgotPasswordUiState(
    val inputFields: Map<ForgotPasswordViewModel.InputFieldType, InputFieldData>,
    val isInputCode: Boolean,
    val isSuccess: Boolean,
    val isLoading: Boolean = false
) {
    companion object {
        fun default(
            inputFields: Map<ForgotPasswordViewModel.InputFieldType, InputFieldData> = emptyMap(),
            isInputCode: Boolean = false,
            isSuccess: Boolean = false,
        ) = ForgotPasswordUiState(
            inputFields = inputFields,
            isInputCode = isInputCode,
            isSuccess = isSuccess
        )
    }
}
