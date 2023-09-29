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

    fun onTextChange(item: InputFieldData, text: String) {
        _uiState.update {
            val newInputFields = it.inputFields.map { inputField ->
                if (inputField.id == item.id)
                    inputField.copy(text = text)
                else inputField
            }
            it.copy(inputFields = newInputFields)
        }
    }

    // TODO: check email and password regex

    private fun generateInputFields() = listOf(
        InputFieldData(
            id = "email",
            label = R.string.email,
            errorMessage = R.string.s11
        ),
        InputFieldData(
            id = "display_name",
            label = R.string.display_name,
        ),
        InputFieldData(
            id = "password",
            label = R.string.password,
            errorMessage = R.string.s6
        ),
        InputFieldData(
            id = "confirm_password",
            label = R.string.confirm,
            errorMessage = R.string.s12
        ),
    )
}