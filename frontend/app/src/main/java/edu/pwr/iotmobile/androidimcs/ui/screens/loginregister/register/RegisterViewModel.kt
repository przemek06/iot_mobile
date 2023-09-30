package edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.register

import androidx.lifecycle.ViewModel
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.InputFieldData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RegisterViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState.default())
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(
                inputFields = generateInputFields()
            )
        }
    }

    fun onTextChange(type: InputFieldType, text: String) {
        _uiState.update {
            val inputField = it.inputFields[type]?.copy(text = text) ?: return
            val newInputFields = it.inputFields.toMutableMap()
            newInputFields.replace(type, inputField)
            it.copy(inputFields = newInputFields)
        }
    }

    // TODO: check email and password regex

    private fun generateInputFields() = mapOf(
        InputFieldType.Email to InputFieldData(
            label = R.string.email,
            errorMessage = R.string.s11
        ),
        InputFieldType.DisplayName to InputFieldData(
            label = R.string.display_name,
        ),
        InputFieldType.Password to InputFieldData(
            label = R.string.password,
            errorMessage = R.string.s6
        ),
        InputFieldType.ConfirmPassword to InputFieldData(
            label = R.string.confirm,
            errorMessage = R.string.s12
        ),
    )

    enum class InputFieldType {
        Email,
        DisplayName,
        Password,
        ConfirmPassword
    }
}