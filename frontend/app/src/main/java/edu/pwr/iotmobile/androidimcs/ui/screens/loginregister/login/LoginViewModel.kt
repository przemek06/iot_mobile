package edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.login

import androidx.lifecycle.ViewModel
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.InputFieldData
import edu.pwr.iotmobile.androidimcs.data.dto.LoginDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState.default())
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

    // TODO: checking and setting errors on login click
    // TODO: creating LoginDto on login click

    private fun generateInputFields() = mapOf(
        InputFieldType.Email to InputFieldData(
            label = R.string.email,
        ),
        InputFieldType.Password to InputFieldData(
            label = R.string.password,
        ),
    )

    private fun InputFieldData.toLoginDto(): LoginDto? {
        val inputFields = uiState.value.inputFields
        return LoginDto(
            email = inputFields[InputFieldType.Email]?.text ?: return null,
            password = inputFields[InputFieldType.Password]?.text ?: return null,
        )
    }

    enum class InputFieldType {
        Email,
        Password
    }
}