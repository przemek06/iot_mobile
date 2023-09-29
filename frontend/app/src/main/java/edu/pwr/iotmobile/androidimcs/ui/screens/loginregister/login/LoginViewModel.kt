package edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.login

import androidx.lifecycle.ViewModel
import edu.pwr.iotmobile.androidimcs.R
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

    private fun generateInputFields() = listOf(
        InputFieldData(
            id = "email",
            label = R.string.email,
        ),
        InputFieldData(
            id = "password",
            label = R.string.password,
        ),
    )
}