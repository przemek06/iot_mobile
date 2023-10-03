package edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.activate

import edu.pwr.iotmobile.androidimcs.data.InputFieldData

data class ActivateAccountUiState(
    val inputField: InputFieldData
) {
    companion object {
        fun default(
            inputField: InputFieldData = InputFieldData()
        ) = ActivateAccountUiState(
            inputField = inputField
        )
    }
}
