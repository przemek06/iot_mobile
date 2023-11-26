package edu.pwr.iotmobile.androidimcs.ui.screens.account

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.MenuOption
import edu.pwr.iotmobile.androidimcs.data.User.Companion.toUser
import edu.pwr.iotmobile.androidimcs.data.dto.UserDto
import edu.pwr.iotmobile.androidimcs.helpers.toast.Toast
import edu.pwr.iotmobile.androidimcs.model.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AccountViewModel(
    private val userRepository: UserRepository,
    private val toast: Toast
) : ViewModel() {

    private val _uiState = MutableStateFlow(AccountUiState.default())
    val uiState = _uiState.asStateFlow()

    fun init(navigation: AccountNavigation) {

        val changePasswordOption = MenuOption(
            titleId = R.string.change_password
        ) { navigation.openChangePassword() }

        viewModelScope.launch {
            try {
                val result = userRepository.getActiveUserInfo()

                if (result.isSuccess) {
                    val user = result.getOrNull()?.toUser() ?: return@launch

                    _uiState.update {
                        it.copy(
                            user = user,
                            changePasswordOption = changePasswordOption,
                            isError = false
                        )
                    }
                } else {
                    Log.d("Account", "Error while getting user data")
                    _uiState.update {
                        it.copy(
                            isError = true
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("Account", "Error while getting user data", e)
                _uiState.update {
                    it.copy(
                        isError = true
                    )
                }
            }
        }
    }

    fun setDisplayName(displayName: String) {
        if (displayName.isBlank()) {
            _uiState.update {
                it.copy(inputField = it.inputField.copy(isError = true))
            }
            return
        }

        viewModelScope.launch {
            try {
                val result = userRepository.updateActiveUser(
                    UserDto(
                        email = uiState.value.user.email,
                        password = "12345678",
                        name = displayName
                    )
                )

                result.onSuccess {
                    val user = userRepository
                        .getActiveUserInfo()
                        .getOrNull()
                        ?.toUser() ?: return@launch

                    _uiState.update {
                        it.copy(
                            user = user,
                            inputField = it.inputField.copy(isError = false)
                        )
                    }

                    toast.toast("Successfully updated display name")
                }
            } catch (e: Exception) {
                Log.e("Account", "Error when updating user data.", e)
                toast.toast("Could not update display name")
            }
        }
    }

    fun deleteAccount(navigation: AccountNavigation) {
        viewModelScope.launch {
            try {
                val result = userRepository.deleteActiveUser()

                result.onSuccess {
                    toast.toast("Successfully deleted account")
                    navigation.openLogin()
                    return@launch
                }
                toast.toast("Failed to delete user account.")
            } catch (e: Exception) {
                Log.e("Account", "Error while logging out.", e)
                toast.toast("Failed to delete user account.")
            }
        }
    }

    fun onTextChange(text: String) {
        _uiState.update {
            it.copy(inputField = it.inputField.copy(text = text))
        }
    }

    fun logout(navigation: AccountNavigation) {
        viewModelScope.launch {
            try {
                val result = userRepository.logout()
                result.onSuccess {
                    toast.toast("Successfully logged out")
                    navigation.openLogin()
                }
                result.onFailure {
                    toast.toast("Could not log out")
                }
            } catch (e: Exception) {
                Log.e("Account", "Error while logging out.", e)
            }
        }
    }
}