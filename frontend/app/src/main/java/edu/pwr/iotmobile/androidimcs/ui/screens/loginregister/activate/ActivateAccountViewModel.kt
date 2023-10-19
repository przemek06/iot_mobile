package edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.activate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun onTextChange(text: String) {
        _uiState.update {
            val inputField = it.inputField.copy(text = text)
            it.copy(inputField = inputField)
        }
    }

    fun onActivate() {
        viewModelScope.launch {
            val code = _uiState.value.inputField
            val result = repository.verifyUser(code.text)
            when (result) {
                ActivateAccountResult.Success -> event.event(ACTIVATE_ACCOUNT_SUCCESS_EVENT)
                ActivateAccountResult.IncorrectCode -> _uiState.update {
                    val updatedInputField = code.copy(isError = true)
                    it.copy(inputField = updatedInputField)
                }
                ActivateAccountResult.Failure -> toast.toast("Error - could not activate account.")
            }
        }
    }

    fun onResendCode() {
        // TODO: send email + show toast message
    }

    companion object {
        const val ACTIVATE_ACCOUNT_SUCCESS_EVENT = "activate_account_success"
    }
}