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
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val uiState = _uiState.value

            val email = uiState.inputFields[InputFieldType.Email]?.text
            val password = uiState.inputFields[InputFieldType.Password]?.text

            if (email.isNullOrBlank() || password.isNullOrBlank()) {
                toast.toast("Email and password cannot be empty.")
                _uiState.update { it.copy(isLoading = false) }
                return@launch
            }

            kotlin.runCatching {
                userRepository.login(
                    email = email,
                    password = password
                )
            }.onSuccess { result ->
                when (result) {
                    LoginUserResult.Success -> event.event(LOGIN_SUCCESS_EVENT)
                    LoginUserResult.AccountInactive -> event.event(LOGIN_ACCOUNT_INACTIVE_EVENT)
                    LoginUserResult.UserBanned -> toast.toast("You have been banned from using this service.")
                    LoginUserResult.Failure -> toast.toast("Email or password incorrect.")
                }
            }.onFailure {
                toast.toast("Error - could not log in.")
            }
            _uiState.update { it.copy(isLoading = false) }
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