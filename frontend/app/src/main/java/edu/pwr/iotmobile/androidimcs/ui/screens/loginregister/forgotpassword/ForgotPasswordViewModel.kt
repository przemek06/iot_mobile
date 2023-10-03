package edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.forgotpassword

import androidx.lifecycle.ViewModel
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.InputFieldData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ForgotPasswordViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ForgotPasswordUiState.default())
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(
                inputFields = generateInputFields()
            )
        }
    }

    fun onTextChange(
        type: InputFieldType,
        text: String
    ) {
        _uiState.update {
            val inputField = it.inputFields[type]?.copy(text = text) ?: return
            val newInputFields = it.inputFields.toMutableMap()
            newInputFields.replace(type, inputField)
            it.copy(inputFields = newInputFields)
        }
    }

    fun onConfirmEmail() {
        // TODO: send email
        _uiState.update {
            it.copy(isInputCode = true)
        }
    }

    fun onResendCode() {
        _uiState.update {
            it.copy(isInputCode = false)
        }
    }

    // TODO: checking and setting errors on confirm click

    private fun generateInputFields() = mapOf(
        InputFieldType.Email to InputFieldData(
            label = R.string.email,
            errorMessage = R.string.s11
        ),
        InputFieldType.Code to InputFieldData(
            label = R.string.password,
            errorMessage = R.string.s5
        ),
        InputFieldType.NewPassword to InputFieldData(
            label = R.string.password,
            errorMessage = R.string.s6
        ),
    )

    enum class InputFieldType {
        Email,
        Code,
        NewPassword
    }
}