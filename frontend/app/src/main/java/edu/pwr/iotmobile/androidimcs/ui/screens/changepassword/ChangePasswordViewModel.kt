package edu.pwr.iotmobile.androidimcs.ui.screens.changepassword

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.dto.PasswordBody
import edu.pwr.iotmobile.androidimcs.helpers.toast.Toast
import edu.pwr.iotmobile.androidimcs.model.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChangePasswordViewModel(
    private val userRepository: UserRepository,
    val toast: Toast
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
        val password = uiState.value.inputFieldPasswordNew.text
        if(password.length < 8) {
            _uiState.update {
                it.copy(
                    inputFieldPasswordNew = it.inputFieldPasswordNew.copy(
                        isError = true,
                        errorMessage = R.string.s6
                    )
                )
            }
            return
        }
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                val result = userRepository.updateActiveUserPassword(PasswordBody(password))

                if (result.isSuccess) {
                    _uiState.update { it.copy(isLoading = false) }
                    navigation.goBack()
                } else {
                    _uiState.update {
                        it.copy(
                            inputFieldPassword = it.inputFieldPassword.copy(
                                isError = true,
                                errorMessage = R.string.s68
                            ),
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("ChangePass", "Failed to change password.", e)
                toast.toast("Operation failed.")
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}