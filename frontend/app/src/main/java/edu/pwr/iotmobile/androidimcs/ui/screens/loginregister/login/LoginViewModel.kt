package edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.login

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.InputFieldData
import edu.pwr.iotmobile.androidimcs.data.result.LoginUserResult
import edu.pwr.iotmobile.androidimcs.helpers.event.Event
import edu.pwr.iotmobile.androidimcs.helpers.toast.Toast
import edu.pwr.iotmobile.androidimcs.model.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userRepository: UserRepository,
    val event: Event,
    val toast: Toast
) : ViewModel() {

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

    fun onLoginClick() {
        viewModelScope.launch {
            val uiState = _uiState.value
            val result = userRepository.login(
                email = uiState.inputFields[InputFieldType.Email]?.text ?: return@launch,
                password = uiState.inputFields[InputFieldType.Password]?.text ?: return@launch,
            )
            when (result) {
                LoginUserResult.Success -> event.event(LOGIN_SUCCESS_EVENT)
                LoginUserResult.AccountInactive -> event.event(LOGIN_ACCOUNT_INACTIVE_EVENT)
                LoginUserResult.Failure -> toast.toast("Error - could not log in.")
            }
        }
    }

    private fun generateInputFields() = mapOf(
        InputFieldType.Email to InputFieldData(
            label = R.string.email,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            )
        ),
        InputFieldType.Password to InputFieldData(
            label = R.string.password,
        ),
    )

    enum class InputFieldType {
        Email,
        Password
    }

    companion object {
        const val LOGIN_SUCCESS_EVENT = "login_success"
        const val LOGIN_ACCOUNT_INACTIVE_EVENT = "account_inactive"
    }
}