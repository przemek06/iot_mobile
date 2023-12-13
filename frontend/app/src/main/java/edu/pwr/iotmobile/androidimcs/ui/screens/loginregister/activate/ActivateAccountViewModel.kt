package edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.activate

import android.util.Log
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.InputFieldData
import edu.pwr.iotmobile.androidimcs.data.result.ActivateAccountResult
import edu.pwr.iotmobile.androidimcs.helpers.event.Event
import edu.pwr.iotmobile.androidimcs.helpers.toast.Toast
import edu.pwr.iotmobile.androidimcs.model.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ActivateAccountViewModel(
    private val repository: UserRepository,
    val event: Event,
    val toast: Toast
) : ViewModel() {

    private val _uiState = MutableStateFlow(ActivateAccountUiState.default())
    val uiState = _uiState.asStateFlow()

    private var _email: String? = null

    fun init(email: String?) {
        if (email != _email) {
            _email = email

            _uiState.update {
                it.copy(
                    inputField = InputFieldData(
                        label = R.string.code,
                        errorMessage = R.string.s5,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        )
                    )
                )
            }
        }
    }

    fun onTextChange(text: String) {
        _uiState.update {
            val inputField = it.inputField.copy(text = text)
            it.copy(inputField = inputField)
        }
    }

    fun onActivate() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val code = _uiState.value.inputField
            when (repository.verifyUser(code.text)) {
                ActivateAccountResult.Success -> {
                    event.event(ACTIVATE_ACCOUNT_SUCCESS_EVENT)
                    toast.toast("Successfully activated account!")
                    _uiState.update {
                        it.copy(
                            isAccountActivated = true,
                            isLoading = false
                        )
                    }
                }
                ActivateAccountResult.IncorrectCode -> _uiState.update {
                    val updatedInputField = code.copy(isError = true)
                    it.copy(
                        inputField = updatedInputField,
                        isLoading = false
                    )
                }
                ActivateAccountResult.Failure -> {
                    toast.toast("Error - could not activate account.")
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    fun onResendCode() {
        val locEmail = _email ?: run {
            viewModelScope.launch {
                toast.toast("Could not resend activation code.")
            }
            return
        }
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val result = repository.resendVerificationCode(locEmail)
                if (result.isSuccess) {
                    toast.toast("Successfully sent a new activation code. Check your email!")
                } else {
                    toast.toast("Could not resend activation code.")
                }
            } catch (e: Exception) {
                Log.e("Activate", "Could not resend activation code.", e)
                toast.toast("Could not resend activation code.")
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    companion object {
        const val ACTIVATE_ACCOUNT_SUCCESS_EVENT = "activate_account_success"
    }
}