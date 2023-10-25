package edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.activate

import edu.pwr.iotmobile.androidimcs.data.InputFieldData

data class ActivateAccountUiState(
    val inputField: InputFieldData,
    val isAccountActivated: Boolean
) {
    companion object {
        fun default(
            inputField: InputFieldData = InputFieldData(),
            isAccountActivated: Boolean = false
        ) = ActivateAccountUiState(
            inputField = inputField,
            isAccountActivated = isAccountActivated
        )
    }
}
