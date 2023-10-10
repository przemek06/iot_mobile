package edu.pwr.iotmobile.androidimcs.ui.screens.changepassword

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ChangePasswordViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ChangePasswordUiState.default())
    val uiState = _uiState.asStateFlow()

    fun onTextChangePassword(text: String) {
        _uiState.update {
            it.copy(inputFieldPassword = it.inputFieldPassword.copy(text = text))
        }
    }
    fun onTextChangePasswordNew(text: String) {
        _uiState.update {
            it.copy(inputFieldPasswordNew = it.inputFieldPasswordNew.copy(text = text))
        }
    }
}