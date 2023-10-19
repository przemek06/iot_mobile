package edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.forgotpassword

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.InputFieldData
import edu.pwr.iotmobile.androidimcs.data.dto.PasswordBody
import edu.pwr.iotmobile.androidimcs.data.result.ForgotPasswordResult
import edu.pwr.iotmobile.androidimcs.helpers.event.Event
import edu.pwr.iotmobile.androidimcs.helpers.toast.Toast
import edu.pwr.iotmobile.androidimcs.model.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(
    private val repository: UserRepository,
    val event: Event,
    val toast: Toast
) : ViewModel() {

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

    fun checkData() {
        val uiState = _uiState.value
        val inputFields = uiState.inputFields
        val newInputFields = inputFields.toMutableMap()
        inputFields.forEach {
            val isValid = when (it.key) {
                InputFieldType.Email ->
                    !uiState.isInputCode && Patterns.EMAIL_ADDRESS.matcher(it.value.text).matches()
                            || uiState.isInputCode
                InputFieldType.NewPassword ->
                    uiState.isInputCode && it.value.text.length >= 8
                            || !uiState.isInputCode
                else -> true
            }
            val inputField = it.value.copy(isError = !isValid)
            newInputFields.replace(it.key, inputField)
        }
        _uiState.update {
            it.copy(inputFields = inputFields)
        }
    }

    fun onConfirmEmail() {
        checkData()
        if (_uiState.value.inputFields.any { it.value.isError }) return

        viewModelScope.launch {
            val email = _uiState.value.inputFields[InputFieldType.Email]?.text ?: return@launch
            val response = repository.sendResetPasswordEmail(email)
            if (response.isSuccess) {
                _uiState.update {
                    it.copy(isInputCode = true)
                }
            } else {
                toast.toast("Error - could not send email.")
            }
        }
    }

    fun onConfirmNewPassword() {
        checkData()
        if (_uiState.value.inputFields.any { it.value.isError }) return

        viewModelScope.launch {
            val inputFields = _uiState.value.inputFields
            val email = inputFields[InputFieldType.Email]?.text ?: return@launch
            val code = inputFields[InputFieldType.Code]?.text ?: return@launch
            val password = inputFields[InputFieldType.NewPassword]?.text ?: return@launch

            val response = repository.resetPassword(
                email = email,
                code = code,
                passwordBody = password.toPasswordBody()
            )
            when (response) {
                ForgotPasswordResult.Success -> event.event(RESET_PASSWORD_SUCCESS_EVENT)
                ForgotPasswordResult.CodeIncorrect -> _uiState.update {
                    val newInputFields = it.inputFields.toMutableMap()
                    val newCodeInputField = it.inputFields[InputFieldType.Code]?.copy(isError = true) ?: return@launch
                    newInputFields.replace(InputFieldType.Code, newCodeInputField)
                    it.copy(inputFields = newInputFields)
                }
                ForgotPasswordResult.Failure -> toast.toast("Error - could not reset password.")
            }
        }
    }

    fun onResendCode() {
        _uiState.update {
            it.copy(isInputCode = false)
        }
    }

    private fun String.toPasswordBody() = PasswordBody(
        password = this
    )

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

    companion object {
        const val RESET_PASSWORD_SUCCESS_EVENT = "reset_password_success"
    }
}