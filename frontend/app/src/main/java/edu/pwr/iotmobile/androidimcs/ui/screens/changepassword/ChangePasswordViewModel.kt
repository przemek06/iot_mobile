package edu.pwr.iotmobile.androidimcs.ui.screens.changepassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.pwr.iotmobile.androidimcs.data.dto.PasswordBody
import edu.pwr.iotmobile.androidimcs.data.dto.UserDto
import edu.pwr.iotmobile.androidimcs.model.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChangePasswordViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

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
    fun onConfirm(navigation: ChangePasswordNavigation) {

        viewModelScope.launch {

            val password = uiState.value.inputFieldPasswordNew.text
            if(password.length < 8) {
                _uiState.update {
                    it.copy(inputFieldPasswordNew = it.inputFieldPasswordNew.copy(isError = true))
                }
                return@launch
            }
            val result = userRepository.updateActiveUserPassword(PasswordBody(password))

            if(result.isSuccess) {
                navigation.goBack()
            } else {
                _uiState.update {
                    it.copy(inputFieldPassword = it.inputFieldPassword.copy(isError = true))
                }
            }
        }
    }
}