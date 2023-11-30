package edu.pwr.iotmobile.androidimcs.ui.screens.changepassword

import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.InputFieldData

data class ChangePasswordUiState (
    val inputFieldPassword: InputFieldData,
    val inputFieldPasswordNew: InputFieldData,
    val isLoading: Boolean = false,
) {
    companion object {
        fun default(
            inputFieldPassword: InputFieldData = InputFieldData(label = R.string.password),
            inputFieldPasswordNew: InputFieldData = InputFieldData(label = R.string.new_password),
        ) = ChangePasswordUiState (
            inputFieldPassword = inputFieldPassword,
            inputFieldPasswordNew = inputFieldPasswordNew
        )
    }
}