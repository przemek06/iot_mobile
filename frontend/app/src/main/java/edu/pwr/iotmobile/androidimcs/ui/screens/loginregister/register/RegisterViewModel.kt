package edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.register

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.InputFieldData
import edu.pwr.iotmobile.androidimcs.data.dto.UserDto
import edu.pwr.iotmobile.androidimcs.data.result.RegisterUserResult
import edu.pwr.iotmobile.androidimcs.helpers.event.Event
import edu.pwr.iotmobile.androidimcs.helpers.toast.Toast
import edu.pwr.iotmobile.androidimcs.model.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val userRepository: UserRepository,
    val event: Event,
    val toast: Toast
) : ViewModel() {

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

    fun checkData() {
        val inputFields = _uiState.value.inputFields
        val newInputFields = inputFields.toMutableMap()
        inputFields.forEach {
            val isValid = when (it.key) {
                InputFieldType.Email -> Patterns.EMAIL_ADDRESS.matcher(it.value.text).matches()
                InputFieldType.Password -> it.value.text.length >= 8
                InputFieldType.ConfirmPassword -> it.value.text == inputFields[InputFieldType.Password]?.text
                InputFieldType.DisplayName -> it.value.text.isNotBlank()
            }
            val inputField = it.value.copy(isError = !isValid)
            newInputFields.replace(it.key, inputField)
        }
        _uiState.update {
            it.copy(inputFields = inputFields)
        }
    }

    fun onRegisterClick() {
        checkData()
        if (_uiState.value.inputFields.any { it.value.isError }) return

        viewModelScope.launch {
            val userDto = _uiState.value.inputFields.toDto() ?: return@launch
            val result = userRepository.register(userDto)
            Log.d("register", "result: ${result.name}")
            when (result) {
                RegisterUserResult.Success -> event.event(REGISTER_SUCCESS_EVENT)
                RegisterUserResult.AccountExists -> event.event(REGISTER_ACCOUNT_EXISTS_EVENT)
                RegisterUserResult.Failure -> toast.toast("Error - could not register.")
            }
        }
    }

    private fun Map<InputFieldType, InputFieldData>.toDto(): UserDto? {
        return UserDto(
            email = this[InputFieldType.Email]?.text ?: return null,
            password = this[InputFieldType.Password]?.text ?: return null,
            name = this[InputFieldType.DisplayName]?.text ?: return null,
        )
    }

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

    companion object {
        const val REGISTER_SUCCESS_EVENT = "register_success"
        const val REGISTER_ACCOUNT_EXISTS_EVENT = "register_account_exists"
    }
}