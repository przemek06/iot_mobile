package edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.activate

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ActivateAccountViewModel() : ViewModel() {

    private val _uiState = MutableStateFlow(ActivateAccountUiState.default())
    val uiState = _uiState.asStateFlow()

    fun onTextChange(text: String) {
        _uiState.update {
            val inputField = it.inputField.copy(text = text)
            it.copy(inputField = inputField)
        }
    }

    fun onActivate() {
        // TODO
    }

    fun onResendCode() {
        // TODO: send email + show toast message
    }
}